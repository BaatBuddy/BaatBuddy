package no.uio.ifi.in2000.team7.boatbuddy

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import org.junit.Test

class SunriseAPITest {

    private val repository: SunriseRepository = SunriseRepository()

    @Test
    fun `test api call returns non null result`() {
        runBlocking {
            val result = repository.getSunriseData("79.00", "80.00", "2024-02-05")
            TestCase.assertNotNull(result)
        }
    }

    @Test

    fun `test api call returns null result`() {
        runBlocking {
            val result = repository.getSunriseData("1000.00", "1000.00", "2024-02-05")
            TestCase.assertNull(result)
        }
    }


}