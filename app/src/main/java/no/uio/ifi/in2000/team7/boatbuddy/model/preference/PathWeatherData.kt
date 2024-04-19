package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class PathWeatherData(
    val date: String,
    // val sunsetAtLastPoint: String?,
    val timeWeatherData: List<TimeWeatherData>,
)
