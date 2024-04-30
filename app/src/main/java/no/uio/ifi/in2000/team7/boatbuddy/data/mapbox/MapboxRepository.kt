package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.autoroute.AutorouteRepository


class MapboxRepository(
) {

    private lateinit var annotationRepository: AnnotationRepository
    private val autorouteRepository = AutorouteRepository()
    private lateinit var mapView: MapView
    private lateinit var context: Context


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
                .zoom(14.0)
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
        MapboxOptions.accessToken = MapboxConstants.token

        mapView = MapView(context)
        this.context = mapView.context
        annotationRepository = AnnotationRepository(mapView)
        onMapReady(cameraOptions = cameraOptions)

        return mapView
    }

    fun onMapReady(cameraOptions: CameraOptions) {

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
            mapboxMap.loadStyle(MapboxConstants.defaultStyle) {
                initLocationComponent()
                setupGesturesListener()
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
                    bearingImage = ImageHolder.from(R.drawable.map_2d_pucker_boat),
                    shadowImage = ImageHolder.from(R.drawable.map_2d_pucker_shadow),
                    scaleExpression = Expression.interpolate {
                        linear()
                        zoom()
                        stop {
                            literal(0.0)
                            literal(0.6)
                        }
                        stop {
                            literal(20.0)
                            literal(1.0)
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
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    suspend fun panToPoint(cameraOptions: CameraOptions) {
        mapView.mapboxMap.flyTo(cameraOptions = cameraOptions)
    }

    // TODO changed
    suspend fun changeStyle(
        style: String
    ) {
        mapView.mapboxMap.loadStyle(style) {}
    }

    suspend fun toggleAlertVisibility() {
        annotationRepository.toggleAlertVisibility()
    }

    fun createLinePath(
        points: List<Point>
    ) {
        annotationRepository.addLineToMap(points = points)
    }

    suspend fun toggleRouteClicking() {
        annotationRepository.toggleRouteClicking()
    }

    suspend fun getRoutePoints(): List<Point> {
        return annotationRepository.getRoutePoints()
    }

    suspend fun generateRoute(): List<Point>? {
        if (annotationRepository.getRoutePoints().size < 2) return null
        val autoroutePoints = autorouteRepository.getAutorouteData(
            course = annotationRepository.route.toList(),
            // TODO needs to retrive data from the database (user profile)
            safetyDepth = "5",
            safetyHeight = "5",
            boatSpeed = "5",
        )

        if (autoroutePoints != null) {
            val formattedPoints = autoroutePoints.geometry.coordinates.map {
                Point.fromLngLat(it[0], it[1])
            }
            annotationRepository.createRoute(formattedPoints)
            return formattedPoints
        }
        return null

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

}