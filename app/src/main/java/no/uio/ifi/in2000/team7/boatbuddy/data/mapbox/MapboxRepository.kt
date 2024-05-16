package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.autoroute.AutorouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class MapboxRepository @Inject constructor(
    private val autorouteRepository: AutorouteRepository,
) {

    private lateinit var annotationRepository: AnnotationRepository
    private lateinit var mapView: MapView
    lateinit var context: Context

    private var _isMapInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // user tracking on map
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .bearing(it)
                .build()
        )
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .zoom(10.0)
                .center(it)
                .build()
        )
        mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }
    }

    fun createMap(context: Context, cameraOptions: CameraOptions): MapView {

        // consider a new solution for this
        MapboxOptions.accessToken = MapboxConstants.TOKEN

        mapView = MapView(context)
        this.context = mapView.context
        annotationRepository = AnnotationRepository(mapView)
        onMapReady(cameraOptions = cameraOptions)

        return mapView
    }

    private fun onMapReady(cameraOptions: CameraOptions) {

        with(mapView) {

            val mapboxMap = this.mapboxMap

            mapboxMap.setCamera(cameraOptions)

            mapboxMap.addOnMoveListener(
                object : OnMoveListener {
                    override fun onMoveBegin(detector: MoveGestureDetector) {
                    }

                    override fun onMove(detector: MoveGestureDetector): Boolean {
                        return false
                    }

                    override fun onMoveEnd(detector: MoveGestureDetector) {
                        annotationRepository.clearViewAnnoations()
                    }
                }
            )

            // improve map later
            // eventually add layers with different functionality
            mapboxMap.loadStyle(MapboxConstants.STYLE) {
                initLocationComponent()
                setupGesturesListener()
                _isMapInitialized.value = true
            }
        }
    }

    private fun initLocationComponent() {
        with(mapView.location) {
            updateSettings {
                puckBearing = PuckBearing.COURSE
                puckBearingEnabled = true
                enabled = true
                locationPuck = LocationPuck2D(
                    bearingImage = ImageHolder.from(R.drawable.map_2d_pucker),
                    shadowImage = ImageHolder.from(R.drawable.map_2d_pucker_shadow),
                    scaleExpression = Expression.interpolate {
                        linear()
                        zoom()
                        stop {
                            literal(0.0)
                            literal(0.45)

                        }
                        stop {
                            literal(15.0)
                            literal(0.75)
                        }
                    }.toJson()
                )
            }
        }
    }

    fun startFollowUserOnMap() {
        with(mapView.location) {
            addOnIndicatorPositionChangedListener(
                onIndicatorPositionChangedListener
            )
            addOnIndicatorBearingChangedListener(
                onIndicatorBearingChangedListener
            )
        }
    }

    private val initializationMutex = Mutex()
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend fun panToUserOnMap() {
        scope.launch {
            initializationMutex.withLock {
                if (::mapView.isInitialized) {
                    with(mapView.location) {
                        addOnIndicatorPositionChangedListener(
                            onIndicatorPositionChangedListener
                        )
                    }
                }
            }
        }
    }

    fun stopFollowUserOnMap() {
        onCameraTrackingDismissed()
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun onCameraTrackingDismissed() {
        with(mapView.location) {
            removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
            removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        }
    }


    suspend fun toggleAlertVisibility() {
        annotationRepository.toggleAlertVisibility()
    }

    fun createLinePath(
        points: List<Point>,
    ) {
        annotationRepository.addLineToMap(points = points)
    }

    fun convertListToPoint(points: List<List<Double>>): List<Point> = points.map {
        Point.fromLngLat(it[0], it[1])
    }

    fun createRoute(points: List<Point>) {
        annotationRepository.createRoute(autoroutePoints = points)
    }

    fun toggleRouteClicking() {
        annotationRepository.toggleRouteClicking()
    }

    fun getRoutePoints(): List<Point> {
        return annotationRepository.getRoutePoints()
    }

    suspend fun fetchRouteData(): APIStatus {
        return autorouteRepository.getAutorouteData(
            course = annotationRepository.route.toList(),
        )
    }

    fun refreshRoute() {
        annotationRepository.refreshRoute()
    }

    fun undoClick() {
        annotationRepository.undoClick()
    }

    fun redoClick() {
        annotationRepository.redoClick()
    }

    suspend fun generateMapURI(points: List<Point>): String {


        val unformattedURL =
            "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/path-%s+%s-%s(%s)/auto/500x300?access_token=%s"

        val encodedStringPath =
            withContext(Dispatchers.IO) {
                URLEncoder.encode(
                    PolylineUtils.encode(points, 5),
                    StandardCharsets.UTF_8.toString()
                )
            }

        return unformattedURL.format(
            4,
            "023047",
            1.0,
            encodedStringPath,
            MapboxConstants.TOKEN
        )
    }

}