package no.uio.ifi.in2000.team7.boatbuddy.data.location

import android.annotation.SuppressLint
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import java.text.SimpleDateFormat

object AlertNotificationCache {
    var featureData: List<FeatureData> = emptyList()
    var enteredAlerts: MutableSet<String> = mutableSetOf() // contains events

    var points: MutableList<Point> = mutableListOf()
    var alertedSunset: Boolean = false
    var sunsetToday: String = ""

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    @SuppressLint("SimpleDateFormat")
    val sunriseDf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    var startTime: String = ""
    var finishTime: String = ""
}