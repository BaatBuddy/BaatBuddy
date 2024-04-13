package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class PathWeatherData(
    val lat: Double,
    val lon: Double,
    val timeWeatherData: List<TimeWeatherData>,
)
