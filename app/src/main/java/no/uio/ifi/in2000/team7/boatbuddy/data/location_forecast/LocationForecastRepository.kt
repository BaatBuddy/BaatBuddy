package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

interface LocationForecastRepo {
    suspend fun getLocationForecastData(
        lat: String,
        lon: String,
        altitude: String = "0"
    ): LocationForecastData?
}

class LocationForecastRepository(
    private val dataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepo {
    override suspend fun getLocationForecastData(
        lat: String, lon: String, altitude: String

    ): LocationForecastData? {

        return dataSource.getLocationForecastData(lat, lon, altitude)
    }

    suspend fun getWeekdayForecastData(
        lat: String,
        lon: String,
        altitude: String
    ): WeekForecast? {
        val locationData = dataSource.getLocationForecastData(lat, lon, altitude)
        if (locationData != null) {
            return WeekForecast(
                days = locationData.timeseries.groupBy {
                    it.time.substring(0, 10)
                }.map {
                    val day = convertDateToDay(it.key)
                    day to DayForecast(
                        date = it.key,
                        day = day,
                        weatherData = it.value
                    )
                }.toMap()
            )
        }
        return null
    }

    private fun convertDateToDay(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        parser.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val day = parser.parse(date)

            val formatter = SimpleDateFormat("EEEE", Locale("no", "NO")) // Norwegian locale
            formatter.timeZone = TimeZone.getTimeZone("CET") // Central European Time

            day?.let { formatter.format(it) } ?: ""
        } catch (e: ParseException) {
            ""
        }
    }


}