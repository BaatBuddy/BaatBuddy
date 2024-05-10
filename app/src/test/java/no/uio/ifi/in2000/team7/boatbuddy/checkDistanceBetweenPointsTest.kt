package no.uio.ifi.in2000.team7.boatbuddy

import com.mapbox.geojson.Point
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt
import kotlin.math.atan2
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.distanceBetweenPoints




class checkDistanceBetweenPointsTest {

    @Test
    fun testDistanceBetweenSamePoint() {
        val point = Point.fromLngLat(0.0, 0.0)
        val distance = distanceBetweenPoints(point, point)
        assertEquals(0.0, distance, 0.001)
    }

    @Test
    fun `test distance between points on equator`() {
        val point1 = Point.fromLngLat(0.0, 0.0)
        val point2 = Point.fromLngLat(1.0, 0.0)
        val distance = distanceBetweenPoints(point1, point2)
        assertEquals(111.319, distance, 0.2)
    }

    @Test
    fun `test distance between points on same longitude`() {
        val point1 = Point.fromLngLat(0.0, 0.0)
        val point2 = Point.fromLngLat(0.0, 1.0)
        val distance = distanceBetweenPoints(point1, point2)
        assertEquals(111.319, distance, 0.2)
    }

}

