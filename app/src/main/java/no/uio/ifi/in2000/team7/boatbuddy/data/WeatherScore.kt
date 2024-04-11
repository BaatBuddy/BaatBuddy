package no.uio.ifi.in2000.team7.boatbuddy.data

import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences

object WeatherScore {


    /*
    Things to include in the algorithm:

        - wave_height                   - negativ (0 - 5+)
        - sea_water_temperature         - positiv (0 - 20+)

        - wind_speed                    - negativ (0 - 12+)
        - wind_speed_of_gust            - negativ (the less the better?)
        - temperature                   - positiv (0 - 30+)
        - cloud_area_fraction           - negativ (0 - 100%)
        - fog_area_fraction             - negativ (0 - 100%)
        - ultraviolet_index_clear_sky   - negativ (0 - 100%)
        - relative_humidity             - negativ (0 - 100%)

    - sunrise
        - sunrise
        - sunset


     */

    // from A - B to a - b with value v
    private fun mapValue(value: Double, fromA: Double, fromB: Double, toA: Double, toB: Double) =
        toA + (value - fromA) * (toB - toA) / (fromB - fromA)

    // maps value from actual data and preference to a score between 0 and 100 (100 being closest to the preferred condition)
    private fun calculateFactor(value: Double, preference: FactorPreference): Double {
        val difference = kotlin.math.abs(preference.value - value)

        return mapValue(difference, preference.from, preference.to, 100.0, 0.0)

    }


    // discuss if the end point should weigh the most

    // creates a score from 0 - 100
    fun calculatePoint(
        oceanData: TimeOceanData,
        forecastData: TimeLocationData,
        weatherPreferences: WeatherPreferences
    ): Double {
        val waveHeight: Double = oceanData.sea_surface_wave_height
        val waterTemperature: Double = oceanData.sea_water_temperature
        val windSpeed: Double = forecastData.wind_speed
        // val windSpeedOfGust: Double = forecastData.wind_speed_of_gust
        val airTemperature: Double = forecastData.air_temperature
        val cloudAreaFraction: Double = forecastData.cloud_area_fraction
        val fogAreaFraction: Double = forecastData.fog_area_fraction
        // val ultravioletIndexClearSky: Double? = forecastData.ultraviolet_index_clear_sky
        val relativeHumidity: Double = forecastData.relative_humidity

        val dataPreference = listOf(
            Pair(waveHeight, weatherPreferences.waveHeight),
            Pair(waterTemperature, weatherPreferences.waterTemperature),
            Pair(windSpeed, weatherPreferences.windSpeed),
            // Pair(windSpeedOfGust, weatherPreferences.windSpeedOfGust),
            Pair(airTemperature, weatherPreferences.airTemperature),
            Pair(cloudAreaFraction, weatherPreferences.cloudAreaFraction),
            Pair(fogAreaFraction, weatherPreferences.fogAreaFraction),
            // Pair(ultravioletIndexClearSky, weatherPreferences.ultravioletIndexClearSky),
            Pair(relativeHumidity, weatherPreferences.relativeHumidity),
        )

        val averageScore = dataPreference.sumOf { pair ->
            val value = pair.first
            val preference = pair.second
            calculateFactor(value, preference)
        } / dataPreference.size

        return averageScore
    }

    fun calculatePath(): Double {


        return 0.0
    }


}