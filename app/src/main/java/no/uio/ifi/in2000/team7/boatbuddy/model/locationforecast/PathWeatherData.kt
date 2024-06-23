package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData

data class PathWeatherData(
    val date: String,
    // val sunsetAtLastPoint: String?,
    val timeWeatherData: List<TimeWeatherData>,
)
