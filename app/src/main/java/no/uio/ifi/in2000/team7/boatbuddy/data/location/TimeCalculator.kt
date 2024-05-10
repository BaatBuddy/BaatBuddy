package no.uio.ifi.in2000.team7.boatbuddy.data.location

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object TimeCalculator {

    @SuppressLint("SimpleDateFormat")
    val sunriseDf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    fun isWithinOneHour(currentTime: String, sunsetTime: String): Boolean {
        if (sunsetTime.isBlank()) return false // time might be null and therefor no sunset
        val formattedCurrentTime = sunriseDf.parse(currentTime.replace("+", ":"))
        val formattedSunsetTime = sunriseDf.parse(sunsetTime.replace("+", ":"))

        if (formattedCurrentTime != null && formattedSunsetTime != null) {
            if (formattedCurrentTime.time - formattedSunsetTime.time > -3600 * 1000) return true
        }

        return false

    }
}