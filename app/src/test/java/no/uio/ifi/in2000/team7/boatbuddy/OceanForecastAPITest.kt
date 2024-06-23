package no.uio.ifi.in2000.team7.boatbuddy

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import org.junit.Assert.assertNull
import org.junit.Test

class OceanForecastAPITest {

    private val repository: OceanForecastRepository = OceanForecastRepository()

    @Test
    fun `test api call returns non null result`() {
        runBlocking {
            val result = repository.getOceanForecastData("79.00", "80.00")
            assertNull(result)
        }
    }

    @Test

    fun `test api call returns null result`() {
        runBlocking {
            val result = repository.getOceanForecastData("1000.00", "1000.00")
            assertNull(result)
        }
    }


}