package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.autoroute.AutorouteRepository


interface MapboxRepo {
    fun createMap(context: Context, cameraOptions: CameraOptions, style: String): MapView
}

class MapboxRepository(
) : MapboxRepo {

    private lateinit var annotationRepository: AnnotationRepository
    private val autorouteRepository = AutorouteRepository()
    private lateinit var routeRepository: RouteRepository
    private lateinit var mapView: MapView

    override fun createMap(context: Context, cameraOptions: CameraOptions, style: String): MapView {

        // consider a new solution for this
        MapboxOptions.accessToken =
            "pk.eyJ1IjoibWFmcmVkcmkiLCJhIjoiY2x1MWIxZ3Q2MGtlZDJrbnhmdTZ0NHZtaSJ9.B6Iawg2wbjSnGqMEOEtxvQ"

        mapView = MapView(context)
        annotationRepository = AnnotationRepository(mapView)
        onMapReady(style = style, cameraOptions = cameraOptions)

        return mapView
    }

    fun onMapReady(style: String, cameraOptions: CameraOptions) {

        with(mapView) {

            val mapboxMap = this.mapboxMap

            mapboxMap.setCamera(cameraOptions)

            /*val annotationApi = this.annotations
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
            var pointsInRoute: MutableList<Point> = mutableListOf()

            mapboxMap.addOnMapClickListener {
                val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
                    .withPoint(it)
                    .withCircleRadius(8.0)
                    .withCircleColor("#ee4e8b")
                    .withCircleStrokeWidth(2.0)
                    .withCircleStrokeColor("#ffffff")
                circleAnnotationManager.create(circleAnnotationOptions)

                if (pointsInRoute.size < 10) {
                    pointsInRoute.add(it)

                    val coordinatesInRoute =
                        pointsInRoute.joinToString(separator = " , ") {
                            "(${it.latitude()}, ${it.longitude()})"
                        }
                    Log.d("Points in route", coordinatesInRoute)
                }

                true
            }*/

            /*val annotationApi = this.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            var pointsInRoute: MutableList<Point> = mutableListOf()

            mapboxMap.addOnMapClickListener {

                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(it)
                pointAnnotationManager.create(pointAnnotationOptions)

                // Ruten kan ikke inneholde mer enn 10 punkter
                if (pointsInRoute.size < 10) {
                    pointsInRoute.add(it)

                    val coordinatesInRoute =
                        pointsInRoute.joinToString(separator = " , ") {
                            "(${it.latitude()}, ${it.longitude()})"
                        }
                    Log.d("Points in route", "$coordinatesInRoute")
                }

                true

            }*/

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
            mapboxMap.loadStyle(style) {

            }


        }
    }

    suspend fun panToPoint(cameraOptions: CameraOptions) {
        mapView.mapboxMap.flyTo(cameraOptions = cameraOptions)
    }

    suspend fun changeStyle(
        style: String
    ) {
        mapView.mapboxMap.loadStyle(style) {}
    }

    suspend fun toggleAlertVisibility() {
        annotationRepository.toggleAlertVisibility()
    }

    suspend fun createLinePath(
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

}