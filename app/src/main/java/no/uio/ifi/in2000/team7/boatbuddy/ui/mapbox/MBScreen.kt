package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.utils.transition
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import no.uio.ifi.in2000.team7.boatbuddy.R
import java.lang.ref.WeakReference

@JvmOverloads
fun loadStyle(style: String, onStyleLoaded: Style.OnStyleLoaded? = null) {

}

@OptIn(MapboxExperimental::class)
@Composable
fun MBScreen() {

    MapboxOptions.accessToken =
        "pk.eyJ1IjoiYWFudW5kaiIsImEiOiJjbHR5Y2FpdnEwY2xsMmtwanFxb3k1Yjk0In0.6jHYW1-ZRQE1EYwM2aQj1A"
    val context = LocalContext.current

    val mapView = MapView(context)

    val annotationApi = mapView.annotations
    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
    val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

    val points = remember { mutableListOf<Point>() }

    with(mapView) {

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(10.20449, 59.74389))
                .zoom(10.0)
                .bearing(0.0)
                .pitch(0.0)
                .build()
        )

        mapboxMap.loadStyle(
            Style.DARK
        ) {
            addAnnotationToMap(
                context,
                Point.fromLngLat(10.20449, 59.74389),
                pointAnnotationManager,
                points,
                polylineAnnotationManager
            )
        }

        mapboxMap.addOnMapClickListener { point ->
            addAnnotationToMap(
                context,
                point,
                pointAnnotationManager,
                points,
                polylineAnnotationManager
            )
            true
        }

    }



    LaunchedEffect(pointAnnotationManager) {
        pointAnnotationManager.addClickListener { clickedAnnotation ->
            pointAnnotationManager.delete(clickedAnnotation)
            points.remove(clickedAnnotation.point)
            points.sortBy { it.latitude() + it.longitude() }

            val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
                .withPoints(points)
                .withLineColor("#219EBC")
                .withLineWidth(4.0)
                .withLineBorderColor("#023047")
                .withLineBorderWidth(1.0)

            polylineAnnotationManager.create(polylineAnnotationOptions)

            true
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            AndroidView(
                factory = { ctx ->
                    mapView
                }
            )
        }

        Button(
            onClick = {
                mapView.mapboxMap.loadStyle(
                    style(Style.STANDARD) {
                        +transition {
                            duration(100L)
                            enablePlacementTransitions(true)
                        }

                        // other runtime styling
                    }
                )
            }
        ) {
            Text(text = "Change Style")
        }

    }

}

/**
 * https://docs.mapbox.com/android/maps/guides/annotations/annotations/
 * https://docs.mapbox.com/android/maps/examples/default-point-annotation/
 */

private fun addAnnotationToMap(
    context: Context,
    point: Point,
    pointAnnotationManager: PointAnnotationManager?,
    points: MutableList<Point>,
    polylineAnnotationManager: PolylineAnnotationManager?
) {
    bitmapFromDrawableRes(
        context,
        R.drawable.ic_map_pin
    )?.let {

        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(it)
            .withIconAnchor(IconAnchor.BOTTOM)

        pointAnnotationManager?.create(pointAnnotationOptions)
        points.add(point)

        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(points)
            .withLineColor("#219EBC")
            .withLineWidth(4.0)
            .withLineBorderColor("#023047")
            .withLineBorderWidth(1.0)

        polylineAnnotationManager?.create(polylineAnnotationOptions)
    }
}

private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
// copying drawable object to not manipulate on the same reference
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}


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
