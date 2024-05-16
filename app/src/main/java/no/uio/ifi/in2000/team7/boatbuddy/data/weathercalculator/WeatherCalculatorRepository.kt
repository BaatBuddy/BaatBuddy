package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfileDao
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateScorePath
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateScoreWeekDay
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.getAvg
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.selectPointsFromPath
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
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

    private suspend fun fetchPathWeatherData(points: List<Point>): List<PathWeatherData> {

        return selectPointsFromPath(points).mapNotNull { point ->
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
                        waveHeight = od.seaSurfaceWaveHeight,
                        waterTemperature = od.seaWaterTemperature,
                        waterDirection = od.seaWaterToDirection,
                        windSpeed = ld.windSpeed,
                        windSpeedOfGust = ld.windSpeedOfGust,
                        airTemperature = ld.airTemperature,
                        cloudAreaFraction = ld.cloudAreaFraction,
                        fogAreaFraction = ld.fogAreaFraction,
                        relativeHumidity = ld.relativeHumidity,
                        precipitationAmount = ld.precipitationAmount,
                        symbolCode = ld.symbolCode
                    )
                } else {
                    TimeWeatherData(
                        lat = lat.toDouble(),
                        lon = lon.toDouble(),
                        time = ld.time,
                        waveHeight = null,
                        waterTemperature = null,
                        waterDirection = null,
                        windSpeed = ld.windSpeed,
                        windSpeedOfGust = ld.windSpeedOfGust,
                        airTemperature = ld.airTemperature,
                        cloudAreaFraction = ld.cloudAreaFraction,
                        fogAreaFraction = ld.fogAreaFraction,
                        relativeHumidity = ld.relativeHumidity,
                        precipitationAmount = ld.precipitationAmount,
                        symbolCode = ld.symbolCode
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
                        waterDirection = if (twd.all { it.waterDirection != null }) getAvg(
                            twd.sumOf { it.waterDirection!! },
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

    suspend fun getWeekdayForecastData(
        points: List<Point>
    ): WeekForecast {
        val pathWeatherData = fetchPathWeatherData(points = points).take(7) // take out 7 days

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

    fun updateWeekForecastScore(weekForecast: WeekForecast): WeekForecast {
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