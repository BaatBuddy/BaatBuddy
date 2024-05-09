package no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location

import android.annotation.SuppressLint
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import java.text.SimpleDateFormat

object AlertNotificationCache {
    var featureData: List<FeatureData> = emptyList()
    var enteredAlerts: MutableSet<String> = mutableSetOf() // contains events

    var points: MutableList<Point> = mutableListOf()
    var alertedSunset: Boolean = false

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    var startTime: String = ""
    var finishTime: String = ""
}