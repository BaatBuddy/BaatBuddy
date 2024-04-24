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
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object WeatherScore {

    // from A - B to a - b with value v
    /*private fun mapValue(value: Double, fromA: Double, fromB: Double, toA: Double, toB: Double) =
        toA + (value - fromA) * (toB - toA) / (fromB - fromA)

    // maps value from actual data and preference to a score between 0 and 100 (100 being closest to the preferred condition)
    private fun calculateFactor(value: Double, preference: FactorPreference): Double {
        val difference = kotlin.math.abs(preference.value - min(value, preference.to + 1))
        return mapValue(difference, preference.from, preference.to, 100.0, 0.0)

    }*/

    // TODO adjust the function to handle null values and add rain. null values means that user doesn't care about the following weather

    // creates a score based on how far away from preferred weather data is (gpt :) )
    fun calculateInstance(realData: Double, preferredData: Double): Double {
        val safePreferredData = if (preferredData == 0.0) 0.01 else preferredData
        val differenceRatio = abs(realData - preferredData) / safePreferredData
        val asd = ((1 - differenceRatio).coerceIn(0.0, 1.0)) * 100
        println(asd.toString() + "UYHEWRGIUYWEGIUWEIUHG")
        return asd
    }

    // creates a score from 0 - 100 based on preference and real data
    fun calculateHour(
        timeWeatherData: TimeWeatherData,
        weatherPreferences: WeatherPreferences,
        // isEndpoint: Boolean,
    ): Double {
        val wantedData = mutableListOf(
            Pair(timeWeatherData.windSpeed, weatherPreferences.windSpeed),
            Pair(timeWeatherData.airTemperature, weatherPreferences.airTemperature),
            Pair(timeWeatherData.cloudAreaFraction, weatherPreferences.cloudAreaFraction),
            Pair(timeWeatherData.waveHeight, 0.5)
        )

        if (weatherPreferences.waterTemperature != null) {
            wantedData.add(
                Pair(timeWeatherData.waterTemperature, weatherPreferences.waterTemperature)
            )
        }
        if (weatherPreferences.relativeHumidity != null) {
            wantedData.add(
                Pair(timeWeatherData.relativeHumidity, weatherPreferences.relativeHumidity)
            )
        }

        return wantedData.sumOf {
            calculateInstance(it.first, it.second)
        } / (wantedData.size + listOf(
            timeWeatherData.precipitationAmount,
            timeWeatherData.fogAreaFraction
        ).count { it != 0.0 })
    }

    fun calculateDate(
        timeWeatherData: List<TimeWeatherData>,
        weatherPreferences: WeatherPreferences
    ): Double {
        val asd = timeWeatherData.sumOf {
            calculateHour(
                it,
                weatherPreferences,
                // it == timeWeatherData.last()
            )
        } / timeWeatherData.size
        println(asd.toString() + "asdasd")
        return asd
    }

    // calculates the amount of points based on the length in km
    suspend fun calculatePath(
        pathWeatherData: List<PathWeatherData>,
        weatherPreferences: WeatherPreferences
    ): List<DateScore> {
        return pathWeatherData.map {
            val asd = DateScore(
                date = it.date,
                score = calculateDate(
                    timeWeatherData = it.timeWeatherData,
                    weatherPreferences = weatherPreferences
                )
            )
            println(asd)
            asd
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