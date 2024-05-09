package no.uio.ifi.in2000.team7.boatbuddy.data.location

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat

object TimeCalculator {

    @SuppressLint("SimpleDateFormat")
    val sunriseDf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    fun isWithinOneHour(currentTime: String, sunsetTime: String): Boolean {
        val formattedCurrentTime = sunriseDf.parse(currentTime)
        val formattedSunsetTime = sunriseDf.parse(sunsetTime)

        if (formattedCurrentTime != null && formattedSunsetTime != null) {
            if (formattedCurrentTime.time - formattedSunsetTime.time > -3600 * 1000) return true
        }



        return false

    }
}