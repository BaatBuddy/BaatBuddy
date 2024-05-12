package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import android.util.Log
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfileDao
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateScorePath
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateScoreWeekDay
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.selectPointsFromPath
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import java.math.RoundingMode
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


class WeatherCalculatorRepository @Inject constructor(
    private val oceanForecastRepository: OceanForecastRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val userDao: UserProfileDao,
) {
    // val sunriseRepository = SunriseRepository()


    suspend fun fetchPathWeatherData(points: List<Point>): List<PathWeatherData> {
        Log.i("ASDASD", "FETCH PATH WEATHER")
        Log.i("ASDASD", points.toString())

        return selectPointsFromPath(points, 40.0).mapNotNull { point ->
            val lat = point.latitude().toString()
            val lon = point.longitude().toString()

            val oceanData = oceanForecastRepository.getOceanForecastData(
                lat = lat,
                lon = lon,
            )
            val locationData = locationForecastRepository.getLocationForecastData(
                point
            )

            locationData?.timeseries?.map { ld ->
                // finds corresponding ocean data
                val od = oceanData?.timeseries?.firstOrNull { tod ->
                    tod.time == ld.time
                }

                if (od != null) {
                    TimeWeatherData(
                        lat = lat.toDouble(),
                        lon = lon.toDouble(),
                        time = ld.time,
                        waveHeight = od.sea_surface_wave_height,
                        waterTemperature = od.sea_water_temperature,
                        windSpeed = ld.wind_speed,
                        windSpeedOfGust = ld.wind_speed_of_gust,
                        airTemperature = ld.air_temperature,
                        cloudAreaFraction = ld.cloud_area_fraction,
                        fogAreaFraction = ld.fog_area_fraction,
                        relativeHumidity = ld.relative_humidity,
                        precipitationAmount = ld.precipitation_amount,
                        symbolCode = ld.symbol_code
                    )
                } else {
                    TimeWeatherData(
                        lat = lat.toDouble(),
                        lon = lon.toDouble(),
                        time = ld.time,
                        waveHeight = null,
                        waterTemperature = null,
                        windSpeed = ld.wind_speed,
                        windSpeedOfGust = ld.wind_speed_of_gust,
                        airTemperature = ld.air_temperature,
                        cloudAreaFraction = ld.cloud_area_fraction,
                        fogAreaFraction = ld.fog_area_fraction,
                        relativeHumidity = ld.relative_humidity,
                        precipitationAmount = ld.precipitation_amount,
                        symbolCode = ld.symbol_code
                    )
                }
            }
        }.flatten().groupBy { twd ->
            twd.time.substring(0, 10)
        }.map { entry ->
            PathWeatherData(
                date = entry.key,
                timeWeatherData = entry.value.groupBy {
                    it.time
                }.map { twdEntry ->
                    val twd = twdEntry.value
                    TimeWeatherData(
                        lat = twd.first().lat,
                        lon = twd.first().lon,
                        time = twd.first().time,
                        waveHeight = if (twd.all { it.waveHeight != null }) getAvg(
                            twd.sumOf { it.waveHeight!! },
                            twd.size
                        ) else null,
                        waterTemperature = if (twd.all { it.waterTemperature != null }) getAvg(
                            twd.sumOf { it.waterTemperature!! },
                            twd.size
                        ) else null,
                        windSpeed = getAvg(twd.sumOf { it.windSpeed }, twd.size),
                        windSpeedOfGust = getAvg(twd.sumOf { it.windSpeedOfGust }, twd.size),
                        airTemperature = getAvg(twd.sumOf { it.airTemperature }, twd.size),
                        cloudAreaFraction = getAvg(twd.sumOf { it.cloudAreaFraction }, twd.size),
                        fogAreaFraction = getAvg(twd.sumOf { it.fogAreaFraction }, twd.size),
                        relativeHumidity = getAvg(twd.sumOf { it.relativeHumidity }, twd.size),
                        precipitationAmount = getAvg(
                            twd.sumOf { it.precipitationAmount },
                            twd.size
                        ),
                        symbolCode = twd[twd.size.floorDiv(2)].symbolCode
                    )
                }
            )
        }
    }

    private fun getAvg(sum: Double, size: Int): Double {
        return (sum / size).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
    }

    suspend fun getWeekdayForecastData(
        points: List<Point>
    ): WeekForecast {
        val pathWeatherData = fetchPathWeatherData(points = points).take(7) // take out 7 days

        // TODO get weather preferences from database
        val weatherPreferences = userDao.getSelectedUser()?.preferences

        val dateScores = calculateScorePath(
            pathWeatherData = pathWeatherData,
            weatherPreferences = weatherPreferences ?: WeatherPreferences(
                windSpeed = 4.0,
                airTemperature = 20.0,
                cloudAreaFraction = 20.0,
                waterTemperature = null,
                relativeHumidity = null,
            ) // use default values if no user selected
        )

        return WeekForecast(
            days = pathWeatherData.mapNotNull {
                if (it.timeWeatherData.any { tld ->
                        tld.time.substring(
                            11,
                            13
                        ) == "12" || tld.time.substring(
                            0,
                            10
                        ) == pathWeatherData.minOf { pw ->
                            pw.timeWeatherData.minOf { twd -> twd.time.substring(0, 10) }
                        }
                    }) {
                    val day = convertDateToDay(it.date.substring(0, 10))

                    it.date to DayForecast(
                        date = it.date,
                        day = day,
                        weatherData = it.timeWeatherData,
                        middayWeatherData = it.timeWeatherData.firstOrNull { twd ->
                            twd.time.substring(11, 13) == "12"
                        } ?: it.timeWeatherData.first(),
                        dayScore = dateScores.firstOrNull { dateScore ->
                            dateScore.date == it.date
                        }
                    )
                } else {
                    null
                }
            }.toMap()

        )
    }

    suspend fun updateWeekForecastScore(weekForecast: WeekForecast): WeekForecast {
        val weatherPreferences = userDao.getSelectedUser()?.preferences

        val dateScores = calculateScoreWeekDay(
            weekForecast = weekForecast,
            weatherPreferences = weatherPreferences ?: WeatherPreferences(
                windSpeed = 4.0,
                airTemperature = 20.0,
                cloudAreaFraction = 20.0,
                waterTemperature = null,
                relativeHumidity = null,
            ) // use default values if no user selected
        )

        return weekForecast.copy(
            days = weekForecast.days.map { entry ->
                entry.key to entry.value.copy(
                    dayScore = dateScores.firstOrNull { dateScore ->
                        dateScore.date == entry.key
                    }
                )
            }.toMap()
        )
    }

    private fun convertDateToDay(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        parser.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val day = parser.parse(date)

            val formatter = SimpleDateFormat("EEEE", Locale("no", "NO")) // Norwegian locale

            formatter.timeZone = TimeZone.getTimeZone("CET") // Central European Time
            day?.let { formatter.format(it).replaceFirstChar { char -> char.uppercase() } }
                ?: "Unknown"
        } catch (e: ParseException) {
            "Unknown"
        }
    }
}