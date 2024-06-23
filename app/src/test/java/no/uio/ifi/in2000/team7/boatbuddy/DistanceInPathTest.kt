package no.uio.ifi.in2000.team7.boatbuddy

import com.mapbox.geojson.Point
import junit.framework.TestCase.assertEquals
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.distanceInPath
import org.junit.Test

class DistanceInPathTest {

    @Test
    fun `test distance in path with no points`() {
        val points = emptyList<Point>()
        val distance = distanceInPath(points)
        assertEquals(0.0, distance)
    }

    @Test
    fun `test distance in path with one point`() {
        val points = listOf(Point.fromLngLat(0.0, 0.0))
        val distance = distanceInPath(points)
        assertEquals(0.0, distance)
    }

    @Test
    fun `test distance in path with two points`() {
        val points = listOf(
            Point.fromLngLat(0.0, 0.0),
            Point.fromLngLat(1.0, 0.0)
        )
        val distance = distanceInPath(points)
        assertEquals(111.319, distance, 0.2)
    }

    @Test
    fun `test distance in path with multiple points`() {
        val points = listOf(
            Point.fromLngLat(0.0, 0.0),
            Point.fromLngLat(1.0, 0.0),
            Point.fromLngLat(1.0, 1.0),
            Point.fromLngLat(0.0, 1.0),
            Point.fromLngLat(0.0, 0.0)
        )
        val distance = distanceInPath(points)
        assertEquals(444.638, distance, 0.2)
    }


}


