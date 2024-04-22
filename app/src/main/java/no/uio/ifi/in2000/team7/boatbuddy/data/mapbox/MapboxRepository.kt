package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import android.util.Log
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener

interface MapboxRepo {
    fun createMap(context: Context, cameraOptions: CameraOptions, style: String): MapView
}

class MapboxRepository(
) : MapboxRepo {

    private lateinit var annotationRepository: AnnotationRepository
    private lateinit var routeRepository: RouteRepository
    private lateinit var mapView: MapView

    override fun createMap(context: Context, cameraOptions: CameraOptions, style: String): MapView {

        // consider a new solution for this
        MapboxOptions.accessToken =
            "pk.eyJ1IjoibWFmcmVkcmkiLCJhIjoiY2x1MWIxZ3Q2MGtlZDJrbnhmdTZ0NHZtaSJ9.B6Iawg2wbjSnGqMEOEtxvQ"

        mapView = MapView(context)
        annotationRepository = AnnotationRepository(mapView)
        routeRepository = RouteRepository(mapView)
        onMapReady(style = style, cameraOptions = cameraOptions)

        return mapView
    }

    fun onMapReady(style: String, cameraOptions: CameraOptions) {

        with(mapView) {
            mapView.mapboxMap.setCamera(cameraOptions)
            
            val annotationApi = this.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager()
            var pointsInRoute: MutableList<Point> = mutableListOf()

            mapboxMap.addOnMapClickListener {

                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(it)
                pointAnnotationManager?.create(pointAnnotationOptions)

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

            }

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

    suspend fun addClickListener(action: Unit) {
        mapView.mapboxMap.addOnMapClickListener {
            run { action }
            true
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

}