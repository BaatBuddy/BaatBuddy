package no.uio.ifi.in2000.team7.boatbuddy

import androidx.compose.ui.util.fastRoundToInt
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateDate
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore.calculateHour
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import org.junit.Test

class WeatherScoreUnitTest {
    @Test
    fun calculatingHourScore_isPrefect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.5,
            waterTemperature = 20.0,
            windSpeed = 4.0,
            windSpeedOfGust = 0.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 30.0,
            precipitationAmount = 0.0,
            symbolCode = "",
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
    fun calculatingHourScoreWithNonDefaultPreferences_isPerfect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.5,
            windSpeed = 4.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            waterTemperature = 20.0,
            relativeHumidity = 30.0,
            precipitationAmount = 0.0,
            fogAreaFraction = 0.0,
            windSpeedOfGust = 0.0,
            symbolCode = "",
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
    fun calculatingHourScore_isNotPerfect() {
        // arrange, create
        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.9,
            waterTemperature = 0.0,
            windSpeed = 3.0,
            windSpeedOfGust = 0.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 0.0,
            precipitationAmount = 0.0,
            symbolCode = "",
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

    @Test
    fun calculatingDayScore_isPerfect() {
        // arrange, create

        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.5,
            waterTemperature = 0.0,
            windSpeed = 4.0,
            windSpeedOfGust = 0.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 0.0,
            precipitationAmount = 0.0,
            symbolCode = "",
        )

        val dayData = listOf(
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
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
        val result = calculateDate(
            timeWeatherData = dayData,
            weatherPreferences = weatherPreferences,
            // isEndpoint = true
        )

        // assert
        assert(result == 100.0)
    }

    @Test
    fun calculatingDayScore_isNotPerfect() {
        // arrange, create

        val timeWeatherData = TimeWeatherData(
            time = "",
            lat = 0.0,
            lon = 0.0,
            waveHeight = 0.5,
            waterTemperature = 0.0,
            windSpeed = 4.0,
            windSpeedOfGust = 0.0,
            airTemperature = 20.0,
            cloudAreaFraction = 20.0,
            fogAreaFraction = 0.0,
            relativeHumidity = 0.0,
            precipitationAmount = 0.0,
            symbolCode = "",
        )

        val dayData = listOf(
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(),
            timeWeatherData.copy(waveHeight = 0.6),
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
        val result = calculateDate(
            timeWeatherData = dayData,
            weatherPreferences = weatherPreferences,
            // isEndpoint = true
        )

        // assert
        assert(result != 100.0)
    }
}