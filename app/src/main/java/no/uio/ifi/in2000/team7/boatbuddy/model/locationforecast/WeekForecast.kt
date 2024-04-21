package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class WeekForecast(
    val days: Map<String, DayForecast>
)
