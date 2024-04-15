package no.uio.ifi.in2000.team7.boatbuddy

import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateHour
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import org.junit.Test

class WeatherScoreUnitTest {
    @Test
    fun calculatingScore_isPrefect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 1.0,
            waterTemperature = 20.0,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 30.0

        )

        val weatherPreferences = WeatherPreferences(
            waveHeight = FactorPreference(
                value = 1.0,
                from = 0.0,
                to = 5.0
            ), waterTemperature = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 20.0
            ), windSpeed = FactorPreference(
                value = 4.0,
                from = 0.0,
                to = 12.0
            ), airTemperature = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 30.0
            ), cloudAreaFraction = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 100.0
            ), fogAreaFraction = FactorPreference(
                value = 0.0,
                from = 0.0,
                to = 100.0
            ), relativeHumidity = FactorPreference(
                value = 30.0,
                from = 0.0,
                to = 100.0
            )
        )

        // act
        val result = calculateHour(
            timeWeatherData = timeWeatherData,
            weatherPreferences = weatherPreferences
        )


        // assert
        assert(result == 100.0)

    }

    @Test
    fun calculatingScoreWithAbovePreferenceRange_isPerfect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 100.0, // above 5 which is max
            waterTemperature = 20.0,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 30.0

        )

        val weatherPreferences = WeatherPreferences(
            waveHeight = FactorPreference(
                value = 6.0, // max value
                from = 0.0,
                to = 5.0
            ), waterTemperature = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 20.0
            ), windSpeed = FactorPreference(
                value = 4.0,
                from = 0.0,
                to = 12.0
            ), airTemperature = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 30.0
            ), cloudAreaFraction = FactorPreference(
                value = 20.0,
                from = 0.0,
                to = 100.0
            ), fogAreaFraction = FactorPreference(
                value = 0.0,
                from = 0.0,
                to = 100.0
            ), relativeHumidity = FactorPreference(
                value = 30.0,
                from = 0.0,
                to = 100.0
            )
        )

        // act
        val result = calculateHour(
            timeWeatherData = timeWeatherData,
            weatherPreferences = weatherPreferences
        )

        // assert
        assert(result == 100.0)


    }
}