package no.uio.ifi.in2000.team7.boatbuddy

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import org.junit.Test

class MetAlertsAPITest {

    private val repository: MetAlertsRepository = MetAlertsRepository()

    @Test
    fun `test api call returns non null result`() {
        runBlocking {
            val result = repository.getMetAlertsData("79.00", "80.00")
            TestCase.assertNotNull(result)
        }
    }


}