package no.uio.ifi.in2000.team7.boatbuddy.data

import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData

object WeatherScore {


    /*
    Things to include in the algorithm:

        - wave_height                   - negativ
        - sea_water_temperature         - positiv

        - wind_speed                    - negativ
        - wind_speed_of_gust            - negativ
        - temperature                   - positiv
        - cloud_area_fraction           - negativ
        - fog_area_fraction             - negativ
        - ultraviolet_index_clear_sky   - negativ
        - relative_humidity             - negativ

    - sunrise
        - sunrise
        - sunset


     */


    // discuss if the end point should weigh the most

    // creates a score from 0 - 100
    fun calculatePoint(
        oceanData: TimeOceanData,
        forecastData: TimeLocationData
    ): Double {
        val waveHeight: Double = oceanData.sea_surface_wave_height
        val waterTemperature: Double = oceanData.sea_water_temperature
        val windSpeed: Double = forecastData.wind_speed
        val windSpeedOfGust: Double = forecastData.wind_speed_of_gust
        val airTemperature: Double = forecastData.air_temperature
        val cloudAreaFraction: Double = forecastData.cloud_area_fraction
        val fogAreaFraction: Double? = forecastData.fog_area_fraction
        val ultravioletIndexClearSky: Double? = forecastData.ultraviolet_index_clear_sky
        val relativeHumidity: Double = forecastData.relative_humidity

        return 0.0
    }

    fun calculatePath(): Double {


        return 0.0
    }


}