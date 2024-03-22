package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

@JvmOverloads
fun loadStyle(style: String, onStyleLoaded: Style.OnStyleLoaded? = null) {

}

@OptIn(MapboxExperimental::class)
@Composable
fun MBScreen() {

    MapboxOptions.accessToken =
        "pk.eyJ1IjoiYWFudW5kaiIsImEiOiJjbHR5Y2FpdnEwY2xsMmtwanFxb3k1Yjk0In0.6jHYW1-ZRQE1EYwM2aQj1A"

    //var mapView: MapView

    MapboxMap(
        Modifier.fillMaxSize(),


        mapViewportState = MapViewportState().apply

        {
            setCameraOptions {
                zoom(11.0)
                center(Point.fromLngLat(10.72, 59.89))
                pitch(0.0)
                bearing(0.0)
            }

            
        }
    ) {


    }


    /**
     * hentet fra https://github.com/mapbox/mapbox-maps-android/blob/v11.0.0/app/src/main/java/com/mapbox/maps/testapp/examples/LocationTrackingActivity.kt
     */

    class LocationTrackingActivity : AppCompatActivity() {

        private lateinit var locationPermissionHelper: LocationPermissionHelper

        private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
            mapView.mapboxMap.setCamera(CameraOptions.Builder().bearing(it).build())
        }

        private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
            mapView.mapboxMap.setCamera(CameraOptions.Builder().center(it).build())
            mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(it)
        }

        private val onMoveListener = object : OnMoveListener {
            override fun onMoveBegin(detector: MoveGestureDetector) {
                onCameraTrackingDismissed()
            }

            override fun onMove(detector: MoveGestureDetector): Boolean {
                return false
            }

            override fun onMoveEnd(detector: MoveGestureDetector) {}
        }
        private lateinit var mapView: MapView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mapView = MapView(this)
            setContentView(mapView)
            locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
            locationPermissionHelper.checkPermissions {
                onMapReady()
            }
        }

        private fun onMapReady() {
            mapView.mapboxMap.setCamera(
                CameraOptions.Builder()
                    .zoom(14.0)
                    .build()
            )
            mapView.mapboxMap.loadStyle(
                Style.STANDARD
            ) {
                initLocationComponent()
                setupGesturesListener()
            }
        }

        private fun setupGesturesListener() {
            mapView.gestures.addOnMoveListener(onMoveListener)
        }

        private fun initLocationComponent() {
            val locationComponentPlugin = mapView.location
            locationComponentPlugin.updateSettings {
                puckBearing = PuckBearing.COURSE
                puckBearingEnabled = true
                enabled = true
                locationPuck = LocationPuck2D(
                    //bearingImage = ImageHolder.from(R.drawable.mapbox_user_puck_icon),
                    //shadowImage = ImageHolder.from(R.drawable.mapbox_user_icon_shadow),
                    scaleExpression = interpolate {
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
            locationComponentPlugin.addOnIndicatorPositionChangedListener(
                onIndicatorPositionChangedListener
            )
            locationComponentPlugin.addOnIndicatorBearingChangedListener(
                onIndicatorBearingChangedListener
            )
        }

        private fun onCameraTrackingDismissed() {
            Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
            mapView.location
                .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
            mapView.location
                .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
            mapView.gestures.removeOnMoveListener(onMoveListener)
        }

        override fun onDestroy() {
            super.onDestroy()
            mapView.location
                .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
            mapView.location
                .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
            mapView.gestures.removeOnMoveListener(onMoveListener)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            locationPermissionHelper.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }
    }

}

class LocationPermissionHelper(val activityRef: WeakReference<Activity>) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        activityRef.get()?.let { activity: Activity ->
            if (PermissionsManager.areLocationPermissionsGranted(activity)) {
                onMapReady()
            } else {
                permissionsManager = PermissionsManager(object : PermissionsListener {

                    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                        activityRef.get()?.let {
                            Toast.makeText(
                                it, "You need to accept location permissions.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionResult(granted: Boolean) {
                        activityRef.get()?.let {
                            if (granted) {
                                onMapReady()
                            } else {
                                it.finish()
                            }
                        }
                    }
                })
                permissionsManager.requestLocationPermissions(activity)
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}