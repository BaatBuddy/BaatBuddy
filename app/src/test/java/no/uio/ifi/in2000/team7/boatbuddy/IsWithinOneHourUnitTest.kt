package no.uio.ifi.in2000.team7.boatbuddy

import no.uio.ifi.in2000.team7.boatbuddy.data.location.TimeCalculator.isWithinOneHour
import org.junit.Test

class IsWithinOneHourUnitTest {

    @Test
    fun IsWithinOneHourSameDay_IsFalse() {
        // copy sunrise format (yyyy-MM-dd'T'hh-mm-ss'Z')
        val currentTime = "2024-05-09T10:00:00Z"
        val sunsetTime = "2024-05-09T11:00:00Z"


        val result = isWithinOneHour(currentTime = currentTime, sunsetTime = sunsetTime)



        assert(result == false)
    }

    @Test
    fun IsWithinOneHourSameDay_IsTrue() {
        // copy sunrise format (yyyy-MM-dd'T'hh-mm-ss'Z')
        val currentTime = "2024-05-09T10:01:00Z"
        val sunsetTime = "2024-05-09T11:00:00Z"


        val result = isWithinOneHour(currentTime = currentTime, sunsetTime = sunsetTime)



        assert(result == true)
    }

    @Test
    fun IsWithinOneHourDifferentDay_IsFalse() {
        // copy sunrise format (yyyy-MM-dd'T'hh-mm-ss'Z')
        val currentTime = "2024-05-08T10:01:00Z"
        val sunsetTime = "2024-05-09T11:00:00Z"


        val result = isWithinOneHour(currentTime = currentTime, sunsetTime = sunsetTime)



        assert(result == false)
    }

    @Test
    fun IsWithinOneHourDifferentDay_IsTrue() {
        // copy sunrise format (yyyy-MM-dd'T'hh-mm-ss'Z')
        val currentTime = "2024-05-10T10:00:00Z"
        val sunsetTime = "2024-05-09T11:00:00Z"


        val result = isWithinOneHour(currentTime = currentTime, sunsetTime = sunsetTime)



        assert(result == true)
    }

}