package no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking

import android.annotation.SuppressLint
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import java.text.SimpleDateFormat

object AlertNotificationCache {
    var featureData: List<FeatureData> = emptyList()
    var enteredAlerts: MutableSet<String> = mutableSetOf() // contains events

    var points: MutableList<Point> = mutableListOf()

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    var start: String = ""
    var end: String = ""
}