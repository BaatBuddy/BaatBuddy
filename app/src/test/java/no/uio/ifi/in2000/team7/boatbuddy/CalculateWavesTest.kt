package no.uio.ifi.in2000.team7.boatbuddy

import junit.framework.TestCase.assertEquals
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateWaves
import org.junit.Test

class CalculateWavesTest {

    @Test
    fun `test waves equal to preferred data`() {
        val realData = 0.5
        val preferredData = 0.5
        val score = calculateWaves(realData, preferredData)
        assertEquals(100.0, score)
    }

    @Test
    fun `test waves slightly above preferred data`() {
        val realData = 0.6
        val preferredData = 0.5
        val score = calculateWaves(realData, preferredData)
        assertEquals(90.0, score)
    }



    @Test
    fun `test waves significantly above preferred data`() {
        val realData = 1.5
        val preferredData = 0.5
        val score = calculateWaves(realData, preferredData)
        assertEquals(0.0, score)
    }


}