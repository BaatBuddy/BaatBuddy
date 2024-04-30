package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures

class MapBoxCardRepository {
    private lateinit var annotationRepository: AnnotationRepository
    private lateinit var mapView: MapView


    fun createMap(context: Context, points: List<Point>): MapView {

        // consider a new solution for this
        MapboxOptions.accessToken = MapboxConstants.token

        mapView = MapView(context)
        annotationRepository = AnnotationRepository(mapView)

        with(mapView.gestures) {
            scrollEnabled = false
            doubleTapToZoomInEnabled = false
            rotateEnabled = false
            pinchScrollEnabled = false
            pinchToZoomEnabled = false
            doubleTouchToZoomOutEnabled = false
            pitchEnabled = false
        }


        val cameraOptions = fitPolylineToScreen(points = points)
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
            mapboxMap.loadStyle(MapboxConstants.defaultStyle) {

            }


        }
    }

    private fun createLinePath(points: List<Point>) {
        annotationRepository.addLineToMap(points = points)
    }

    fun fitPolylineToScreen(points: List<Point>): CameraOptions {
        createLinePath(points = points)

        val southwest =
            Point.fromLngLat(points.minOf { it.longitude() }, points.minOf { it.latitude() })

        val northeast =
            Point.fromLngLat(points.maxOf { it.longitude() }, points.maxOf { it.latitude() })

        val cameraOptions = mapView.mapboxMap.cameraForCoordinateBounds(
            bounds = CoordinateBounds(southwest, northeast),
            boundsPadding = EdgeInsets(0.0, 0.0, 0.0, 0.0)
        )

        val testCameraOptions = CameraOptions.Builder()
            .zoom(cameraOptions.zoom?.plus(2.0))
            .center(cameraOptions.center)
            .build()

        mapView.mapboxMap.easeTo(
            cameraOptions = testCameraOptions
        )

        return cameraOptions
    }
}