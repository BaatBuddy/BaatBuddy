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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationOptions
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.utils.transition
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotation
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import java.lang.ref.WeakReference

@JvmOverloads
fun loadStyle(style: String, onStyleLoaded: Style.OnStyleLoaded? = null) {

}

@OptIn(MapboxExperimental::class)
@Composable
fun MBScreen(
    locationViewModel: UserLocationViewModel = viewModel(),
    metAlertsViewModel: MetAlertsViewModel
) {
    // fetches all alerts (no arguments)
    metAlertsViewModel.initialize()
    val metAlertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()

    MapboxOptions.accessToken =
        "pk.eyJ1IjoibWFmcmVkcmkiLCJhIjoiY2x1MWIxZ3Q2MGtlZDJrbnhmdTZ0NHZtaSJ9.B6Iawg2wbjSnGqMEOEtxvQ"

    val context = LocalContext.current

    val mapView = MapView(context)

    // initialize all annotation managers
    val annotationApi = mapView.annotations
    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
    val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
    val polygonAnnotationManager = annotationApi.createPolygonAnnotationManager()
    val viewAnnotationManager = mapView.viewAnnotationManager

    val selectedPoint = remember { mutableListOf<PointAnnotation>() }
    val metalertPolygon = remember { mutableListOf<MetalertPolygon>() }

    var alertVisible by remember { mutableStateOf(false) }

    with(mapView) {

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(10.20449, 59.74389))
                .zoom(10.0)
                .bearing(0.0)
                .pitch(0.0)
                .build()
        )

        mapboxMap.loadStyle("mapbox://styles/mafredri/clu8bbhvh019501p71sewd7eg") {

        }

        mapboxMap.addOnMapClickListener { point ->
            if (selectedPoint.isNotEmpty()) {
                pointAnnotationManager.annotations.firstOrNull { it.point == selectedPoint.first().point }
                    ?.let { pointAnnotationManager.delete(it) }
                selectedPoint.removeAll { true }
            }

            val pointAnnotation = addPinToMap(
                context = context,
                point = point,
                pointAnnotationManager = pointAnnotationManager
            )

            if (pointAnnotation != null) {
                selectedPoint.add(pointAnnotation)
                mapboxMap.flyTo(
                    cameraOptions = CameraOptions.Builder()
                        .center(pointAnnotation.point)
                        .build()
                ) // consider removing flyto
            }
            true
        }

        mapboxMap.addOnMoveListener(
            object : OnMoveListener {
                override fun onMoveBegin(detector: MoveGestureDetector) {

                    if (viewAnnotationManager.annotations.isNotEmpty()) {
                        viewAnnotationManager.removeAllViewAnnotations()
                    }
                }

                override fun onMove(detector: MoveGestureDetector): Boolean {
                    return false
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {

                }
            }
        )


    }


    // creates a on click event that deletes existing pins on the map
    LaunchedEffect(polygonAnnotationManager) {

        polygonAnnotationManager.addClickListener { clickedPolygons ->
            clickedPolygons.points.forEach { polygon ->
                val centroid = Point.fromLngLat(
                    polygon.sumOf { point -> point.longitude() } / polygon.size, //lon
                    polygon.sumOf { point -> point.latitude() } / polygon.size // lat
                )

                if (viewAnnotationManager.annotations.isNotEmpty()) {
                    viewAnnotationManager.removeAllViewAnnotations()
                }


                val viewAnnotation = viewAnnotationManager.addViewAnnotation(
                    resId = R.layout.metalert_view_card,
                    ViewAnnotationOptions.Builder()
                        .geometry(centroid)
                        .build()
                )

                mapView.mapboxMap.flyTo(
                    cameraOptions = mapView.mapboxMap.cameraForGeometry(
                        geometry = Polygon.fromLngLats(clickedPolygons.points),
                        geometryPadding = EdgeInsets(10.0, 3.0, 3.0, 3.0)
                    )
                )

            }


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
        Row(

        ) {
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
            Button(
                onClick = {
                    alertVisible = !alertVisible
                    val polygons = polygonAnnotationManager.annotations

                    if (alertVisible) {
                        // consider if it's valid to have this logic in UI layer

                        if (polygons.isNotEmpty()) {
                            polygons.forEach { it.fillOpacity = 0.4 }
                            polygonAnnotationManager.update(polygons)
                        }

                        metAlertsUIState.metalerts?.features?.forEach { featureData ->
                            featureData.affected_area.forEach { area ->

                                val alertArea = area.map { polygon ->
                                    polygon.map { coordinate ->
                                        Point.fromLngLat(coordinate[0], coordinate[1])
                                    }
                                }
                                if (alertArea !in polygons.map { it.points }) {
                                    val polygon = addPolygonToMap(
                                        polygonAnnotationManager = polygonAnnotationManager,
                                        points = alertArea,
                                        fColor = featureData.riskMatrixColor
                                    )

                                    MetalertPolygon(
                                        polygon = polygon,
                                        featureData = featureData
                                    )
                                }
                            }
                        }
                    } else {
                        polygons.forEach { it.fillOpacity = 0.0 }
                        polygonAnnotationManager.update(polygons)
                    }

                }
            ) {
                Text(text = "${if (alertVisible) "Hide" else "Show"} Alerts")
            }

            Button(
                onClick = {
                    addLineToMap(
                        polylineAnnotationManager = polylineAnnotationManager,
                        points =
                        listOf(
                            Point.fromLngLat(10.668749999999989, 59.89375000000001),
                            Point.fromLngLat(10.668749999999989, 59.87291666666667),
                            Point.fromLngLat(10.647916666666674, 59.852083333333326),
                            Point.fromLngLat(10.627083333333331, 59.83125000000001),
                            Point.fromLngLat(10.606249999999989, 59.81041666666667),
                            Point.fromLngLat(10.585416666666674, 59.789583333333326),
                            Point.fromLngLat(10.585416666666674, 59.76875000000001),
                            Point.fromLngLat(10.564583333333331, 59.74791666666667),
                            Point.fromLngLat(10.564583333333331, 59.727083333333326),
                            Point.fromLngLat(10.585416666666674, 59.70625000000001),
                            Point.fromLngLat(10.606249999999989, 59.68541666666667),
                            Point.fromLngLat(10.606249999999989, 59.664583333333326),
                            Point.fromLngLat(10.606249999999989, 59.64375000000001),
                            Point.fromLngLat(10.627083333333331, 59.62291666666667),
                            Point.fromLngLat(10.627083333333331, 59.602083333333326),
                            Point.fromLngLat(10.627083333333331, 59.58125000000001),
                            Point.fromLngLat(10.606249999999989, 59.56041666666667),
                            Point.fromLngLat(10.585416666666674, 59.539583333333326),
                            Point.fromLngLat(10.564583333333331, 59.539583333333326),
                            Point.fromLngLat(10.543749999999989, 59.51875000000001),
                            Point.fromLngLat(10.522916666666674, 59.51875000000001),
                            Point.fromLngLat(10.502083333333331, 59.51875000000001),
                            Point.fromLngLat(10.481249999999989, 59.51875000000001),
                            Point.fromLngLat(10.460416666666674, 59.51875000000001),
                            Point.fromLngLat(10.439583333333331, 59.51875000000001),
                            Point.fromLngLat(10.418749999999989, 59.51875000000001),
                            Point.fromLngLat(10.397916666666674, 59.539583333333326),
                            Point.fromLngLat(10.418749999999989, 59.56041666666667),
                            Point.fromLngLat(10.418749999999989, 59.58125000000001),
                            Point.fromLngLat(10.418749999999989, 59.602083333333326),
                            Point.fromLngLat(10.418749999999989, 59.62291666666667),
                            Point.fromLngLat(10.418749999999989, 59.64375000000001),
                            Point.fromLngLat(10.397916666666674, 59.664583333333326),
                            Point.fromLngLat(10.377083333333331, 59.68541666666667),
                            Point.fromLngLat(10.356249999999989, 59.70625000000001),
                            Point.fromLngLat(10.335416666666674, 59.70625000000001),
                            Point.fromLngLat(10.314583333333331, 59.70625000000001),
                            Point.fromLngLat(10.293749999999989, 59.70625000000001),
                            Point.fromLngLat(10.272916666666674, 59.727083333333326),
                            Point.fromLngLat(10.252083333333331, 59.727083333333326),
                        )
                    )
                }
            ) {
                Text(text = "Add path")
            }


        }
        Button(
            onClick = {
                val location = locationViewModel.fetchUserLocation(context)
                if (location != null) {
                    mapView.mapboxMap.flyTo(
                        CameraOptions.Builder()
                            .center(Point.fromLngLat(location.longitude, location.latitude))
                            .zoom(18.0)
                            .bearing(location.bearing.toDouble())
                            .pitch(45.0)
                            .build()
                    )
                }
            }
        ) {
            Text(text = "Fly to user")
        }

    }

}

/**
 * https://docs.mapbox.com/android/maps/guides/annotations/annotations/
 * https://docs.mapbox.com/android/maps/examples/default-point-annotation/
 */


// function to add a point to the map and to the mutable points list
private fun addPinToMap(
    context: Context,
    point: Point,
    pointAnnotationManager: PointAnnotationManager
): PointAnnotation? {
    bitmapFromDrawableRes(
        context,
        R.drawable.ic_map_pin
    )?.let { bitmap -> // map-pin icon

        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconAnchor(IconAnchor.BOTTOM)

        return pointAnnotationManager.create(pointAnnotationOptions)

    }
    return null
}

// function to create a polyline on the map based on a list of points, creates lines based on the order of points
private fun addLineToMap(
    polylineAnnotationManager: PolylineAnnotationManager,
    points: List<Point>,
) {

    val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
        .withPoints(points)
        .withLineColor("#219EBC")
        .withLineWidth(4.0)
        .withLineBorderColor("#023047")
        .withLineBorderWidth(1.0)

    polylineAnnotationManager.create(polylineAnnotationOptions)

}

data class MetalertPolygon(
    val polygon: PolygonAnnotation,
    val featureData: FeatureData
)

private fun addPolygonToMap(
    polygonAnnotationManager: PolygonAnnotationManager,
    points: List<List<Point>>,
    fColor: String = "",
    olColor: String = ""
): PolygonAnnotation {
    // string in hex value format
    val fillColor: String
    val outlineColor: String

    // when block to handle input from metalerts risk matrix colors
    when (fColor) {
        "Yellow" -> {
            fillColor = "#ffd500"
            outlineColor = "#8f7700"
        }

        "Orange" -> {
            fillColor = "#ff9100"
            outlineColor = "#8c4f00"
        }

        "Red" -> {
            fillColor = "#ff0d00"
            outlineColor = "#870700"
        }

        // default color green
        "" -> {
            fillColor = "#80ff00"
            outlineColor = "#478f00"
        }

        // if other color is supplied
        else -> {
            // make a test for a valid color format
            fillColor = fColor
            outlineColor = olColor
        }
    }

    val polygonAnnotationOptions: PolygonAnnotationOptions = PolygonAnnotationOptions()
        .withPoints(points)
        .withFillColor(fillColor)
        .withFillOpacity(0.4)
        .withFillOutlineColor(outlineColor)


    return polygonAnnotationManager.create(polygonAnnotationOptions)
}


// functions to convert a xml vector to a bitmap object
private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
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
