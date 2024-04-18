package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener

interface MapboxRepo {
    fun createMap(context: Context, cameraOptions: CameraOptions, style: String): MapView
}

class MapboxRepository(
) : MapboxRepo {

    private lateinit var annotationRepository: AnnotationRepository
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
            mapView.mapboxMap.setCamera(cameraOptions)

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

}