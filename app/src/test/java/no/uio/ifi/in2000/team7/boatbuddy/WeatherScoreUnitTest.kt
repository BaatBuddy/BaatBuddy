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
            waveHeight = 0.5,
            waterTemperature = 20.0,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 30.0,
            precipitationAmount = 0.0
        )

        val weatherPreferences = WeatherPreferences(
            waveHeight = 0.5,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            waterTemperature = null,
            relativeHumidity = null,
            precipitationAmount = 0.0,
            fogAreaFraction = 0.0
        )

        // act
        val result = calculateHour(
            timeWeatherData = timeWeatherData,
            weatherPreferences = weatherPreferences,
            // isEndpoint = true
        )


        // assert
        assert(result == 100.0)

    }

    @Test
    fun calculatingScoreWithNonDefaultPreferences_isPerfect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.5,
            waterTemperature = 20.0,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 30.0,
            precipitationAmount = 0.0
        )

        val weatherPreferences = WeatherPreferences(
            waveHeight = 0.5,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            waterTemperature = 20.0,
            relativeHumidity = 30.0,
            precipitationAmount = 0.0,
            fogAreaFraction = 0.0
        )

        // act
        val result = calculateHour(
            timeWeatherData = timeWeatherData,
            weatherPreferences = weatherPreferences,
            // isEndpoint = true
        )

        // assert
        assert(result == 100.0)


    }

    @Test
    fun calculatingScore_isNotPerfect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 100000.0,
            waterTemperature = 2000.0,
            windSpeed = 4000.0,
            airTemperature = 20000.0,
            cloudAreaFraction = 20000.0,
            fogAreaFraction = 10000.0,
            relativeHumidity = 3000.0,
            precipitationAmount = 10000.0

        )

        val weatherPreferences = WeatherPreferences(
            waveHeight = 0.5,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            waterTemperature = null,
            relativeHumidity = null,
            precipitationAmount = 0.0,
            fogAreaFraction = 0.0
        )

        // act
        val result = calculateHour(
            timeWeatherData = timeWeatherData,
            weatherPreferences = weatherPreferences,
            // isEndpoint = true
        )

        // assert
        assert(result != 100.0)


    }
}