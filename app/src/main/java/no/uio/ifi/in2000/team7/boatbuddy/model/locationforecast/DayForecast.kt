package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class DayForecast(
    val date: String,
    val day: String,
    val weatherData: List<TimeLocationData>
)
