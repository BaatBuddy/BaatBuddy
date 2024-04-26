package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculatePath
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class WeatherCalculatorRepository {
    val oceanForecastRepository = OceanForecastRepository()
    val locationForecastRepository = LocationForecastRepository()
    // val sunriseRepository = SunriseRepository()


    suspend fun fetchPathWeatherData(points: List<Point>): List<PathWeatherData> {
        return points.mapNotNull { point ->
            val lat = point.latitude().toString()
            val lon = point.longitude().toString()

            val oceanData = oceanForecastRepository.getOceanForecastData(
                lat = lat,
                lon = lon,
            )
            val locationData = locationForecastRepository.getLocationForecastData(
                lat = lat,
                lon = lon,
            )

            locationData?.timeseries?.mapNotNull { ld ->
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
                    null
                }
            }
        }.flatten().groupBy { twd ->
            twd.time.substring(0, 10)
        }.map {
            val lastPoint = points.last()
            PathWeatherData(
                date = it.key,
                //sunsetAtLastPoint = sunriseRepository.getSunriseData(
                //    lastPoint.latitude().toString(), lastPoint.longitude().toString(), it.key
                //)?.sunsetTime,
                timeWeatherData = it.value
            )
        }
    }

    suspend fun getWeekdayForecastData(
        points: List<Point>
    ): WeekForecast {
        val weekWeatherData = fetchPathWeatherData(points = points)

        // TODO get weather preferences from database
        val dateScores = calculatePath(
            pathWeatherData = weekWeatherData, weatherPreferences = WeatherPreferences(
                windSpeed = 0.0,
                airTemperature = 0.0,
                cloudAreaFraction = 0.0,
                waterTemperature = null,
                relativeHumidity = null,
            )
        )

        // List<PathWeatherData>
        // PathWeatherData
        //    val date: String,
        //    // val sunsetAtLastPoint: String?,
        //    val timeWeatherData: List<TimeWeatherData>,

        return WeekForecast(
            days = weekWeatherData.mapNotNull {
                if (it.timeWeatherData.any { tld ->
                        tld.time.substring(
                            11,
                            13
                        ) == "12" || tld.time.substring(
                            0,
                            10
                        ) == weekWeatherData.minOf { pw ->
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