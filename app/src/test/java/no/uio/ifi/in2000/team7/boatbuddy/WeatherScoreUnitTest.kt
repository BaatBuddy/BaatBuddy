package no.uio.ifi.in2000.team7.boatbuddy

import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherScore.calculatePoint
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import org.junit.Test

class WeatherScoreUnitTest {
    @Test
    fun calculatingScore_isPrefect() {
        // arrange, create
        val oceanData = TimeOceanData(
            time = "",
            sea_surface_wave_from_direction = 0.0,
            sea_surface_wave_height = 1.0,
            sea_water_speed = 0.0,
            sea_water_temperature = 20.0,
            sea_water_to_direction = 0.0
        )

        val forecastData = TimeLocationData(
            time = "",
            air_pressure_at_sea_level = 0.0,
            air_temperature = 20.0,
            cloud_area_fraction = 20.0,
            fog_area_fraction = 0.0,
            ultraviolet_index_clear_sky = null,
            relative_humidity = 30.0,
            wind_from_direction = 0.0,
            wind_speed = 4.0,
            wind_speed_of_gust = 0.0
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
        val result = calculatePoint(
            oceanData = oceanData,
            forecastData = forecastData,
            weatherPreferences = weatherPreferences
        )


        // assert
        assert(result == 100.0)

    }
}