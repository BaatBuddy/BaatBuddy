package no.uio.ifi.in2000.team7.boatbuddy

import com.mapbox.geojson.Point
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import org.junit.Test

class LocationForecastAPITest {

    private val repository: LocationForecastRepository = LocationForecastRepository()

    @Test
    fun `test api call returns non null result`() {
        runBlocking {
            val point = Point.fromLngLat(79.00, 80.00)
            val result = repository.getLocationForecastData(point)
            assertNotNull(result)
        }
    }

    @Test
    fun `test api call returns null result`() {
        runBlocking {
            val point = Point.fromLngLat(1000.00, 1000.00) //Illegal location
            val result = repository.getLocationForecastData(point)
            assertNull(result)
        }
    }

    @Test
    fun `test api call returns another null result`() {
        runBlocking {
            val point = Point.fromLngLat(-1000.00, -1000.00) //Illegal location
            val result = repository.getLocationForecastData(point)
            assertNull(result)
        }
    }

    @Test
    fun `test api call returns notnull`() {
        runBlocking {
            val point = Point.fromLngLat(-90.00, -90.00)
            val result = repository.getLocationForecastData(point)
            assertNotNull(result)
        }
    }

}