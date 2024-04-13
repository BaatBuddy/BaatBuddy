package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import android.util.Log
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.DateScore
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object WeatherScore {


    /*
    Things to include in the algorithm:

        - wave_height                   - negativ (0 - 5+)
        - sea_water_temperature         - positiv (0 - 20+)

        - wind_speed                    - negativ (0 - 12+)
        - wind_speed_of_gust            - negativ (the less the better?)
        - temperature                   - positiv (0 - 30+)
        - cloud_area_fraction           - negativ (0 - 100%)
        - fog_area_fraction             - negativ (0 - 100%)
        - ultraviolet_index_clear_sky   - negativ (0 - 100%)
        - relative_humidity             - negativ (0 - 100%)

    - sunrise
        - sunrise
        - sunset


     */

    // from A - B to a - b with value v
    private fun mapValue(value: Double, fromA: Double, fromB: Double, toA: Double, toB: Double) =
        toA + (value - fromA) * (toB - toA) / (fromB - fromA)

    // maps value from actual data and preference to a score between 0 and 100 (100 being closest to the preferred condition)
    private fun calculateFactor(value: Double, preference: FactorPreference): Double {
        val difference = kotlin.math.abs(preference.value - value)

        return mapValue(difference, preference.from, preference.to, 100.0, 0.0)

    }


    // discuss if the end point should weigh the most

    // creates a score from 0 - 100
    fun calculateHour(
        timeWeatherData: TimeWeatherData,
        weatherPreferences: WeatherPreferences
    ): Double {
        val waveHeight: Double = timeWeatherData.waveHeight
        val waterTemperature: Double = timeWeatherData.waterTemperature
        val windSpeed: Double = timeWeatherData.windSpeed
        // val windSpeedOfGust: Double = timeWeatherData.winSpeedGust
        val airTemperature: Double = timeWeatherData.airTemperature
        val cloudAreaFraction: Double = timeWeatherData.cloudAreaFraction
        val fogAreaFraction: Double = timeWeatherData.fogAreaFraction
        // val ultravioletIndexClearSky: Double? = timeWeatherData.ultravioletIndexClearSky
        val relativeHumidity: Double = timeWeatherData.relativeHumidity

        val dataPreference = listOf(
            Pair(waveHeight, weatherPreferences.waveHeight),
            Pair(waterTemperature, weatherPreferences.waterTemperature),
            Pair(windSpeed, weatherPreferences.windSpeed),
            // Pair(windSpeedOfGust, weatherPreferences.windSpeedOfGust),
            Pair(airTemperature, weatherPreferences.airTemperature),
            Pair(cloudAreaFraction, weatherPreferences.cloudAreaFraction),
            Pair(fogAreaFraction, weatherPreferences.fogAreaFraction),
            // Pair(ultravioletIndexClearSky, weatherPreferences.ultravioletIndexClearSky),
            Pair(relativeHumidity, weatherPreferences.relativeHumidity),
        )

        val averageScore = dataPreference.sumOf { pair ->
            val value = pair.first
            val preference = pair.second
            calculateFactor(value, preference)
        } / dataPreference.size

        return averageScore
    }

    fun calculateDate(
        timeWeatherData: List<TimeWeatherData>,
        weatherPreferences: WeatherPreferences
    ): Double {
        return timeWeatherData.sumOf {
            calculateHour(
                it,
                weatherPreferences
            )
        } / timeWeatherData.size
    }

    // calculates the amount of points based on the length in km
    fun calculatePath(
        pathWeatherData: List<PathWeatherData>,
        weatherPreferences: WeatherPreferences
    ): List<DateScore> {
        return pathWeatherData.flatMap { it.timeWeatherData }.groupBy {
            it.time.substring(0, 10)
        }.map {
            DateScore(it.key, calculateDate(it.value, weatherPreferences))
        }
    }


    // pick out points based on the length of the path and distance between each point
    fun selectPointsFromPath(points: List<Point>): List<Point> {
        Log.i("ASDASD", "Distance of path in km: ${distanceInPath(points)}")
        val distance = distanceInPath(points)
        val nBetweenPoints =
            max(distance.div(40).toInt(), 2) // at least 1 point between start and end
        Log.i("ASDASD", nBetweenPoints.toString())
        val distanceBetweenPoints = distance / nBetweenPoints

        val outPoints = mutableListOf(points.first())
        var pointer = points.first()
        for (i in 1..<nBetweenPoints) {
            val newPoint = points.first {
                distanceBetweenPoints(pointer, it) > distanceBetweenPoints && it !in outPoints
            }
            outPoints.add(newPoint)
            pointer = newPoint
        }
        outPoints.add(points.last())
        Log.i("ASDASD", outPoints.toString())
        return outPoints.toList()

    }


    // converts distance between two geopoints to km
    private fun distanceBetweenPoints(start: Point, end: Point): Double {
        val R = 6371.0 // Radius of the Earth in kilometers

        val lat1Rad = Math.toRadians(start.latitude())
        val lon1Rad = Math.toRadians(start.longitude())
        val lat2Rad = Math.toRadians(end.latitude())
        val lon2Rad = Math.toRadians(end.longitude())

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


}