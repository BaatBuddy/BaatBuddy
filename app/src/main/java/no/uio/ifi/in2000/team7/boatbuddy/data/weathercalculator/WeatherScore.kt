package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.DateScore
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.times

object WeatherScore {

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

        var sumScore = 0.0
        var factors = 4

        sumScore += calculateSpeed(timeWeatherData.windSpeed, weatherPreferences.windSpeed)
        sumScore += calculateTemp(timeWeatherData.airTemperature, weatherPreferences.airTemperature)
        sumScore += calculatePercentages(
            timeWeatherData.cloudAreaFraction,
            weatherPreferences.cloudAreaFraction
        )
        if (timeWeatherData.waveHeight != null) {
            sumScore += calculateWaves(timeWeatherData.waveHeight, 0.5)
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

        return sumScore / (factors + listOf(
            timeWeatherData.precipitationAmount,
            timeWeatherData.fogAreaFraction
        ).count { it != 0.0 }) // takes down the score if they are not equal to 0.0 which is ideal
    }

    fun calculateDate(
        timeWeatherData: List<TimeWeatherData>,
        weatherPreferences: WeatherPreferences
    ): Double {
        return timeWeatherData.sumOf {
            calculateHour(
                it,
                weatherPreferences,
                // it == timeWeatherData.last()
            )
        } / timeWeatherData.size

    }

    // calculates the amount of points based on the length in km
    suspend fun calculatePath(
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

    // pick out points based on the length of the path and distance between each point
    fun selectPointsFromPath(points: List<Point>): List<Point> {
        val distance = distanceInPath(points)
        val nBetweenPoints =
            max(distance.div(40).toInt(), 2) // at least 1 point between start and end
        val distanceBetweenPoints = distance / nBetweenPoints
        val outPoints = mutableListOf(points.first())

        var pointer = points.first()
        points.fold(0.0) { total, point ->
            val distanceFromLast = distanceBetweenPoints(pointer, point)
            val addedDistance = total + distanceFromLast
            if (addedDistance == distanceBetweenPoints) {
                outPoints.add(point)
                pointer = point
                0.0
            } else if (addedDistance > distanceBetweenPoints) {
                val intermediatePoint = intermediatePoint(
                    first = pointer,
                    second = point,
                    nKmFromFirst = distanceFromLast - (addedDistance - distanceBetweenPoints)
                )
                outPoints.add(intermediatePoint)
                pointer = point
                addedDistance - distanceBetweenPoints
            } else {
                pointer = point
                addedDistance
            }
        }
        outPoints.add(points.last())

        return outPoints.toList()

    }

    // converts distance between two geopoints to km (gpt / website)
    private fun distanceBetweenPoints(first: Point, second: Point): Double {
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
    private fun distanceInPath(points: List<Point>): Double {
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