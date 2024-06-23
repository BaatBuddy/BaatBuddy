package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

import no.uio.ifi.in2000.team7.boatbuddy.model.preference.DateScore
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData

data class WeekForecast(
    val days: Map<String, DayForecast>
)

data class DayForecast(
    val date: String,
    val day: String,
    val weatherData: List<TimeWeatherData>,
    val middayWeatherData: TimeWeatherData,
    val dayScore: DateScore?,
)
