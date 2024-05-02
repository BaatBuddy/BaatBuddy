package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.ViewAnnotationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotation
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotation
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotation
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.viewannotation.geometry
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.convertAlertResId
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.convertLanguage

class AnnotationRepository(
    private val mapView: MapView
) {
    private val metAlertsRepository = MetAlertsRepository()
    private val alertPolygons = mutableListOf<AlertPolygon>()

    private val annotationApi = mapView.annotations
    private val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
    private val polygonAnnotationManager = annotationApi.createPolygonAnnotationManager()
    private val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
    private val viewAnnotationManager = mapView.viewAnnotationManager

    private var isAlertClickable = false
    private var alertData: List<FeatureData>? = null

    private val undoList: MutableList<Pair<Point, CircleAnnotation>> =
        mutableListOf()
    private val redoList: MutableList<Pair<Point, CircleAnnotation>> =
        mutableListOf()
    private var removeFromList: Pair<Point, CircleAnnotation>? = null

    private var isSelectingRoute = false
    val route: MutableList<Point> = mutableListOf()

    init {
        runBlocking {
            addPolygonClickListener()
            addRouteClickListener()
        }
    }

    // functions for the polyline manager
    fun addLineToMap(
        points: List<Point>, // order of point matter
    ): PolylineAnnotation {

        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(points)
            .withLineColor("#219EBC")
            .withLineWidth(4.0)
            .withLineBorderColor("#023047")
            .withLineBorderWidth(1.0)

        return polylineAnnotationManager.create(polylineAnnotationOptions)
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
            .withFillOpacity(0.4)
            .withFillOutlineColor(outlineColor)

        return polygonAnnotationManager.create(polygonAnnotationOptions)
    }

    suspend fun addAlertPolygons() {
        val polygons = polygonAnnotationManager.annotations
        if (alertData == null) {
            val fetchedData = metAlertsRepository.getMetAlertsData(
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
            }
            alertData = fetchedData
        } else {
            alertData
        }


        alertData?.forEach { featureData ->
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
    }

    suspend fun toggleAlertVisibility() {
        isAlertClickable = !isAlertClickable
        if (!isAlertClickable) {
            polygonAnnotationManager.deleteAll()
            viewAnnotationManager.removeAllViewAnnotations()
        } else {
            addAlertPolygons()
        }
    }

    // click listener for metalert polygon
    suspend fun addPolygonClickListener() {
        val context = mapView.context
        polygonAnnotationManager.addClickListener { clickedPolygon ->
            if (isAlertClickable) {
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
                convertAlertResId(
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

    suspend fun toggleRouteClicking() {
        isSelectingRoute = !isSelectingRoute
    }

    private fun addRouteClickListener() {
        mapView.mapboxMap.addOnMapClickListener { point ->
            if (isSelectingRoute) {
                userClick(point = point)
            }
            true
        }

        circleAnnotationManager.addClickListener { clickedCircle ->
            val pair = Pair(clickedCircle.point, clickedCircle)
            if (isSelectingRoute) {
                route.remove(clickedCircle.point)
                undoList.remove(pair)
                circleAnnotationManager.delete(clickedCircle)
                polylineAnnotationManager.deleteAll()
                addLineToMap(route)
            }
            true
        }
    }

    private fun userClick(point: Point) {
        var circle: CircleAnnotation?
        if (route.size < 10) {
            removeFromList = redoList.lastOrNull()
            circle = addCircleToMap(point)
            route.add(point)
            if (route.size > 1) {
                polylineAnnotationManager.deleteAll()
                addLineToMap(route)
            }
            undoList.add(Pair(point, circle))
        }
    }

    private fun addCircleToMap(point: Point): CircleAnnotation {
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            .withPoint(point)
            .withCircleRadius(8.0)
            .withCircleColor("#ee4e8b")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#ffffff")
        return circleAnnotationManager.create(circleAnnotationOptions)
    }

    suspend fun getRoutePoints(): List<Point> {
        return route
    }

    fun createRoute(autoroutePoints: List<Point>) {
        polylineAnnotationManager.deleteAll()
        addLineToMap(autoroutePoints)
    }

    fun refreshRoute() {
        route.clear()
        undoList.clear()
        redoList.clear()
        circleAnnotationManager.deleteAll()
        polylineAnnotationManager.deleteAll()
    }

    fun undoClick() {
        val undo = undoList.removeLastOrNull()
        undo?.let {
            redoList.add(
                Pair(
                    it.first,
                    it.second,
                )
            )
            route.remove(it.first)
            circleAnnotationManager.delete(it.second)
            polylineAnnotationManager.deleteAll()
            addLineToMap(route)
        }
    }

    fun redoClick() {
        val redo = redoList.removeLastOrNull()
        redo?.let {
            if (redo != removeFromList) {
                val newCircle = addCircleToMap(it.first)
                route.add(it.first)
                undoList.add(
                    Pair(
                        it.first,
                        newCircle,
                    )
                )
            }
            polylineAnnotationManager.deleteAll()
            addLineToMap(route)
        }
    }

}