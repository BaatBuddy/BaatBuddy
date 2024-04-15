package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.AutoRouteViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import java.lang.ref.WeakReference


@OptIn(MapboxExperimental::class)
@Composable
fun MBScreen(
    locationViewModel: UserLocationViewModel = viewModel(),
    metAlertsViewModel: MetAlertsViewModel,
    mapboxViewModel: MapboxViewModel,
    autorouteViewModel: AutoRouteViewModel = viewModel()
) {
    val context = LocalContext.current

    // fetches all alerts (no arguments)
    metAlertsViewModel.initialize()
    mapboxViewModel.initialize(
        context = context,
        cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(10.20449, 59.74389))
            .zoom(10.0)
            .bearing(0.0)
            .pitch(0.0)
            .build(),
        style = "mapbox://styles/mafredri/cluwbjt8q000q01quhopi4g0m"
    )


    val boatSpeed = "5" // knop
    val boatHeight = "5"
    val safetyDepth = "5"
    val course = listOf<Point>(
        Point.fromLngLat(10.707517, 59.879888),
        Point.fromLngLat(8.788321, 58.431549)
    )
    autorouteViewModel.initialize(course, boatSpeed, boatHeight, safetyDepth)

    val metAlertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val autorouteUIState by autorouteViewModel.autoRouteUiState.collectAsState()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            if (mapboxUIState != null) {
                AndroidView(
                    factory = { ctx ->
                        mapboxUIState.mapView
                    }
                )
            }
        }
        Row() {
            Button(
                onClick = {
                    Log.i("ASDASD", Style.STANDARD + "\n" + Style.DARK + "\n" + mapboxUIState.style)
                    if (mapboxUIState.style != Style.STANDARD) {
                        mapboxViewModel.changeStyle(Style.STANDARD)
                    } else {
                        mapboxViewModel.changeStyle(Style.DARK)
                    }
                }
            ) {
                Text(text = "Change Style")
            }
            Button(
                onClick = {
                    mapboxViewModel.toggleAlertVisibility()
                }
            ) {
                Text(text = "${if (mapboxUIState.alertVisible) "Hide" else "Show"} Alerts")
            }
        }
        Button(
            onClick = {
                val location = locationViewModel.fetchUserLocation(context)
                if (location != null) {
                    mapboxViewModel.panToPoint(
                        cameraOptions = CameraOptions.Builder()
                            .center(Point.fromLngLat(location.longitude, location.latitude))
                            .bearing(location.bearing.toDouble())
                            .build()
                    )
                }
            }
        ) {
            Text(text = "Fly to user")
        }

        Button(
            onClick = {
                autorouteUIState.autoRoute?.geometry?.coordinates?.map {
                    Point.fromLngLat(it[0], it[1])
                }?.let {
//                    mapboxViewModel.createLinePath(it)
                    mapboxViewModel.createLinePath(WeatherScore.selectPointsFromPath(it))
                    mapboxViewModel.calculateWeather(WeatherScore.selectPointsFromPath(it))
                }

            }
        ) {
            Text(text = "Create path")
        }

    }

}

/**
 * https://docs.mapbox.com/android/maps/guides/annotations/annotations/
 * https://docs.mapbox.com/android/maps/examples/default-point-annotation/
 */


// hentet fra https://github.com/mapbox/mapbox-maps-android/blob/v11.0.0/app/src/main/java/com/mapbox/maps/testapp/examples/LocationTrackingActivity.kt

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
