package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.DateScore
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import java.math.RoundingMode
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object WeatherScore {

    fun mapValue(v: Double, A: Double, B: Double, a: Double, b: Double): Double {
        return a + (v - A) * (b - a) / (B - A)
    }

    fun getColor(score: Double): Color {
        val scaledValue = (mapValue(score, 0.0, 100.0, 255.0, 0.0) * 1.5).toInt().coerceIn(0, 255)

        return Color(scaledValue, 255 - scaledValue, 0)
    }

    fun calculateWaves(realData: Double, preferredData: Double): Double {
        // set ratio between difference of data to score -> 0.01m = 1score
        val diff = abs(realData - preferredData)
        val ratio = 0.01

        return max(
            (100 - diff / ratio),
            0.0
        ) * if (realData < preferredData) 1.5 else 1.0 // everything above 1,5m waves gives 0 but under 0.5 is preferred
    }

    fun calculatePercentages(
        realData: Double,
        preferredData: Double
    ): Double { // used for cloud area and humidity
        return 100 - abs(realData - preferredData)
    }

    fun calculateTemp(realData: Double, preferredData: Double): Double {
        val diff = abs(realData - preferredData)
        val ratio = 0.15

        return (100 - diff / ratio).coerceIn(
            0.0,
            100.0
        ) // makes sure that the data is between 0 and 100
    }

    fun calculateSpeed(realData: Double, preferredData: Double): Double {
        val diff = abs(realData - preferredData)
        val ratio = 0.15

        return (100 - diff / ratio).coerceIn(
            0.0,
            100.0
        ) // makes sure that the data is between 0 and 100
    }

    // creates a score from 0 - 100 based on preference and real data
    fun calculateHour(
        timeWeatherData: TimeWeatherData,
        weatherPreferences: WeatherPreferences,
        // isEndpoint: Boolean,
    ): Double {

        var sumScore = 0.0 // compensation for weather at night
        var factors = 3

        sumScore += calculateSpeed(timeWeatherData.windSpeed, weatherPreferences.windSpeed)
        sumScore += calculateTemp(timeWeatherData.airTemperature, weatherPreferences.airTemperature)
        sumScore += calculatePercentages(
            timeWeatherData.cloudAreaFraction,
            weatherPreferences.cloudAreaFraction
        )
        if (timeWeatherData.waveHeight != null) {
            sumScore += calculateWaves(timeWeatherData.waveHeight, 0.5)
            factors += 1
        }


        if (weatherPreferences.waterTemperature != null && timeWeatherData.waterTemperature != null) {
            sumScore += calculateTemp(
                timeWeatherData.waterTemperature,
                weatherPreferences.waterTemperature
            )
            factors += 1
        }
        if (weatherPreferences.relativeHumidity != null) {
            sumScore += calculatePercentages(
                timeWeatherData.relativeHumidity,
                weatherPreferences.relativeHumidity
            )
            factors += 1
        }

        return ((sumScore / (factors + listOf(
            timeWeatherData.precipitationAmount,
            timeWeatherData.fogAreaFraction
        ).count { it != 0.0 } * 2)) + 10).coerceIn(
            0.0,
            100.0
        ) // takes down the score if they are not equal to 0.0 which is ideal
    }

    fun calculateDate(
        timeWeatherData: List<TimeWeatherData>,
        weatherPreferences: WeatherPreferences
    ): Double {
        val formattedTWD = selectWeatherDataFromDay(timeWeatherData)
        Log.i("ASDASD", formattedTWD.toString())
        return formattedTWD.sumOf {
            calculateHour(
                it,
                weatherPreferences,
                // it == timeWeatherData.last()
            )
        } / formattedTWD.size

    }

    fun selectWeatherDataFromDay(twd: List<TimeWeatherData>): List<TimeWeatherData> {
        if (twd.size > 4) {
            return twd.map {
                it.copy(
                    time = when (it.time.substring(11, 13).toInt()) {
                        in 0..5 -> "0-5"
                        in 6..11 -> "6-11"
                        in 12..17 -> "12-17"
                        in 18..23 -> "18-23"
                        else -> "outside"
                    }
                )
            }.groupBy { it.time }.map { entry ->
                // could be in a function
                TimeWeatherData(
                    lat = entry.value.first().lat,
                    lon = entry.value.first().lon,
                    time = entry.value.first().time,
                    waveHeight = if (entry.value.all { it.waveHeight != null }) getAvg(
                        entry.value.sumOf { it.waveHeight!! },
                        entry.value.size
                    ) else null,
                    waterTemperature = if (entry.value.all { it.waterTemperature != null }) getAvg(
                        entry.value.sumOf { it.waterTemperature!! },
                        entry.value.size
                    ) else null,
                    waterDirection = if (entry.value.all { it.waterDirection != null }) getAvg(
                        entry.value.sumOf { it.waterDirection!! },
                        entry.value.size
                    ) else null,
                    windSpeed = getAvg(entry.value.sumOf { it.windSpeed }, entry.value.size),
                    windSpeedOfGust = getAvg(
                        entry.value.sumOf { it.windSpeedOfGust },
                        entry.value.size
                    ),
                    airTemperature = getAvg(
                        entry.value.sumOf { it.airTemperature },
                        entry.value.size
                    ),
                    cloudAreaFraction = getAvg(
                        entry.value.sumOf { it.cloudAreaFraction },
                        entry.value.size
                    ),
                    fogAreaFraction = getAvg(
                        entry.value.sumOf { it.fogAreaFraction },
                        entry.value.size
                    ),
                    relativeHumidity = getAvg(
                        entry.value.sumOf { it.relativeHumidity },
                        entry.value.size
                    ),
                    precipitationAmount = getAvg(
                        entry.value.sumOf { it.precipitationAmount },
                        entry.value.size
                    ),
                    symbolCode = entry.value[entry.value.size.floorDiv(2)].symbolCode
                )
            }
        }
        return twd
    }

    fun getAvg(sum: Double, size: Int): Double {
        return (sum / size).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
    }

    // calculates the amount of points based on the length in km
    suspend fun calculateScorePath(
        pathWeatherData: List<PathWeatherData>,
        weatherPreferences: WeatherPreferences
    ): List<DateScore> {
        return pathWeatherData.map {
            DateScore(
                date = it.date,
                score = calculateDate(
                    timeWeatherData = it.timeWeatherData,
                    weatherPreferences = weatherPreferences
                )
            )
        }
    }

    suspend fun calculateScoreWeekDay(
        weekForecast: WeekForecast,
        weatherPreferences: WeatherPreferences
    ): List<DateScore> {
        return weekForecast.days.map { entry ->
            DateScore(
                date = entry.key,
                score = calculateDate(
                    timeWeatherData = entry.value.weatherData,
                    weatherPreferences = weatherPreferences,
                )
            )
        }
    }

    // pick out points based on the length of the path and distance between each point (40km default)
    fun selectPointsFromPath(points: List<Point>): List<Point> {
        if (points.size in 1..2) return points
        val distance = distanceInPath(points)

        val outPoints = mutableListOf(points.first())

        var pointer = points.first()
        points.fold(0.0) { total, point ->
            val distanceFromLast = distanceBetweenPoints(pointer, point)
            val addedDistance = total + distanceFromLast
            if (addedDistance > distance / 2) {
                val intermediatePoint = intermediatePoint(
                    first = pointer,
                    second = point,
                    nKmFromFirst = addedDistance - total
                )
                outPoints.add(intermediatePoint)
                outPoints.add(points.last())
                return outPoints
            } else {
                pointer = point
                addedDistance
            }
        }
        outPoints.add(points.last())
        return outPoints

    }

    // converts distance between two geopoints to km (gpt / website)
    fun distanceBetweenPoints(first: Point, second: Point): Double {
        val R = 6371.0 // Radius of the Earth in kilometers

        val lat1Rad = Math.toRadians(first.latitude())
        val lon1Rad = Math.toRadians(first.longitude())
        val lat2Rad = Math.toRadians(second.latitude())
        val lon2Rad = Math.toRadians(second.longitude())

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2);
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }

    // gets total distance in a list of points
    fun distanceInPath(points: List<Point>): Double {
        return points.zipWithNext().sumOf { (current, next) ->
            distanceBetweenPoints(current, next)
        }
    }

    // function to return point based on distance from first of two points in km (gpt)
    private fun intermediatePoint(first: Point, second: Point, nKmFromFirst: Double): Point {

        val radius = 6371.0 // Earth's Radius in Kms

        // Convert latitudes from degrees to radians
        val lat1 = first.latitude() * PI / 180.0
        val lon1 = first.longitude() * PI / 180.0
        val lat2 = second.latitude() * PI / 180.0
        val lon2 = second.longitude() * PI / 180.0

        // Computing the total distance between two geo points using Haversine law
        val a =
            sin((lat2 - lat1) / 2).pow(2.0) + cos(lat1) * cos(lat2) * sin((lon2 - lon1) / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val totalDistance = radius * c

        // Calculate the ratio
        val A = sin((1 - (nKmFromFirst / totalDistance)) * c) / sin(c)
        val B = sin((nKmFromFirst / totalDistance) * c) / sin(c)

        // Calculating intermediary point's coordinates
        val x = A * cos(lat1) * cos(lon1) + B * cos(lat2) * cos(lon2)
        val y = A * cos(lat1) * sin(lon1) + B * cos(lat2) * sin(lon2)
        val z = A * sin(lat1) + B * sin(lat2)

        val lat3 = atan2(z, sqrt(x * x + y * y)) * 180 / PI
        val lon3 = atan2(y, x) * 180 / PI

        return Point.fromLngLat(lon3, lat3)
    }


}



