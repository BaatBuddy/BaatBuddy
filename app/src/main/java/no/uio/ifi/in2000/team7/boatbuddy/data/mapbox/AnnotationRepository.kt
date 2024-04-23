package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.ViewAnnotationOptions
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotation
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.viewannotation.geometry
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.ui.IconConverter
import no.uio.ifi.in2000.team7.boatbuddy.ui.IconConverter.bitmapFromDrawableRes
import no.uio.ifi.in2000.team7.boatbuddy.ui.IconConverter.convertLanguage

class AnnotationRepository(
    val mapView: MapView
) {
    val metAlertsRepository = MetAlertsRepository()
    private val alertPolygons = mutableListOf<AlertPolygon>()

    private val annotationApi = mapView.annotations
    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
    val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
    private val polygonAnnotationManager = annotationApi.createPolygonAnnotationManager()
    val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
    private val viewAnnotationManager = mapView.viewAnnotationManager

    private var isClickable = false

    private var isSelectingRoute = false

    init {
        runBlocking {
            val polygons = polygonAnnotationManager.annotations
            metAlertsRepository.getMetAlertsData(
                lat = "",
                lon = ""
            )?.features?.sortedBy { featureData ->
                when (featureData.riskMatrixColor.lowercase()) {
                    "green" -> "1"
                    "yellow" -> "2"
                    "orange" -> "3"
                    "red" -> "4"
                    else -> "0"
                }
            }?.forEach { featureData ->
                featureData.affected_area.forEach { area ->

                    val alertArea = area.map { polygon ->
                        polygon.map { coordinate ->
                            Point.fromLngLat(coordinate[0], coordinate[1])
                        }
                    }
                    if (alertArea !in polygons.map { it.points }) {
                        val polygon = addPolygonToMap(
                            points = alertArea,
                            fColor = featureData.riskMatrixColor
                        )

                        val polygonAlert = AlertPolygon(
                            polygonAnnotation = polygon,
                            featureData = featureData
                        )

                        alertPolygons.add(polygonAlert)
                    }
                }
            }
            addPolygonClickListener()
            addRouteClickListener()
        }


    }


    // functions for point manager
    private fun addPinToMap(
        point: Point
    ): PointAnnotation? {
        bitmapFromDrawableRes(
            mapView.context,
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


    // functions for the polyline manager
    suspend fun addLineToMap(
        points: List<Point>, // order of point matter
    ) {

        points.forEach {
            addPinToMap(
                it
            )
        }

        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(points)
            .withLineColor("#219EBC")
            .withLineWidth(4.0)
            .withLineBorderColor("#023047")
            .withLineBorderWidth(1.0)

        polylineAnnotationManager.create(polylineAnnotationOptions)

    }

    // functions for the polygon manager
    private fun addPolygonToMap(
        points: List<List<Point>>,
        fColor: String = "",
        olColor: String = ""
    ): PolygonAnnotation {
        // string in hex value format
        val fillColor: String
        val outlineColor: String

        // when-block to handle input from metalerts risk matrix colors
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
            .withFillOpacity(0.0)
            .withFillOutlineColor(outlineColor)

        return polygonAnnotationManager.create(polygonAnnotationOptions)
    }

    suspend fun toggleAlertVisibility() {
        polygonAnnotationManager.annotations.forEach {
            if (it.fillOpacity == 0.0) {
                it.fillOpacity = 0.4
                isClickable = true
            } else {
                it.fillOpacity = 0.0
                isClickable = false
                clearViewAnnoations()
            }
            polygonAnnotationManager.update(it)
        }
    }

    suspend fun addPolygonClickListener() {
        // click listener for metalert polygon

        val context = mapView.context
        polygonAnnotationManager.addClickListener { clickedPolygon ->
            if (isClickable) {
                clickedPolygon.points.forEach { polygon ->
                    // consider using another formula to find centroid of a polygon
                    val centroid = Point.fromLngLat(
                        polygon.sumOf { point -> point.longitude() } / polygon.size, //lon
                        polygon.sumOf { point -> point.latitude() } / polygon.size // lat
                    )

                    this.clearViewAnnoations()

                    val alertPolygon = alertPolygons.firstOrNull { alertPolygon ->
                        alertPolygon.polygonAnnotation.points == clickedPolygon.points
                    }

                    alertPolygon?.featureData?.let {
                        val metalertCardView = this.createCardView(
                            context = context,
                            featureData = it
                        )

                        viewAnnotationManager.addViewAnnotation(
                            metalertCardView,
                            ViewAnnotationOptions.Builder()
                                .geometry(centroid)
                                .build()
                        )

                        val geoCamera = mapView.mapboxMap.cameraForGeometry(
                            geometry = Polygon.fromLngLats(clickedPolygon.points),
                            geometryPadding = EdgeInsets(100.0, 100.0, 100.0, 100.0)
                        )

                        mapView.mapboxMap.flyTo(
                            cameraOptions = geoCamera
                        )

                    }

                }
            }
            true

        }

    }

    // functions for the view manager
    fun clearViewAnnoations() {
        if (viewAnnotationManager.annotations.isNotEmpty()) {
            viewAnnotationManager.removeAllViewAnnotations()
        }
    }

    fun createCardView(
        context: Context,
        featureData: FeatureData
    ): LinearLayout {
        val metalertCardView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundResource(R.drawable.card_shape)
            setPadding(10, 10, 10, 10)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val cardHeader = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val cardName = TextView(context).apply {
            text = convertLanguage(featureData.event)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val cardAlertIcon = ImageView(context).apply {
            setImageResource(
                IconConverter.convertAlertResId(
                    event = featureData.event,
                    riskMatrixColor = featureData.riskMatrixColor,
                    context = context
                )
            )
        }

        cardHeader.addView(cardName)
        cardHeader.addView(cardAlertIcon)
        metalertCardView.addView(cardHeader)

        return metalertCardView
    }

    // creating route section
    fun addRouteClickListener() {
        mapView.mapboxMap.addOnMapClickListener(
            
        )
    }

    fun userClick(point: Point) {
        addCircleToMap(point)
    }

    private fun addCircleToMap(point: Point) {
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            .withPoint(point)
            .withCircleRadius(8.0)
            .withCircleColor("#ee4e8b")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#ffffff")
        circleAnnotationManager.create(circleAnnotationOptions)
    }


}