package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import java.net.UnknownHostException

class LocationForecastDataSource {

    //Tar imot Lat, lon og altitude
    suspend fun getLocationForecastData(
        lat: String,
        lon: String,
        altitude: String,
    ): LocationForecastData? {

        // construct url
        var path = "weatherapi/locationforecast/2.0/complete"

        if (lat.isNotBlank() && lon.isNotBlank()) {
            path += "?lat=%s&lon=%s".format(lat, lon)
            if (altitude.isNotBlank()) {
                path += "&altitude=%s".format(altitude)
            }
        }

        // attempt fetching data
        return try {
            //order of args -> lat, lon, altitude
            val results = client.get(path)
            if (results.status.value !in 200..299) return null

            val data: LocationForecastAPI = results.body()

            val coordinates = data.geometry.coordinates
            val timeseries = data.properties.timeseries

            // transform data into more usable data
            LocationForecastData(
                lon = coordinates[0],
                lat = coordinates[1],
                timeseries = timeseries.mapNotNull { timesery ->
                    val nextHours = timesery.data
                    if (nextHours.next_6_hours != null) {
                        val details = timesery.data.instant.details
                        TimeLocationData(
                            time = timesery.time,
                            airPressureAtSeaLevel = details.air_pressure_at_sea_level,
                            airTemperature = details.air_temperature,
                            cloudAreaFraction = details.cloud_area_fraction,
                            fogAreaFraction = details.fog_area_fraction ?: 0.0,
                            ultravioletIndexClearSky = details.ultraviolet_index_clear_sky,
                            relativeHumidity = details.relative_humidity,
                            windFromDirection = details.wind_from_direction,
                            windSpeed = details.wind_speed,
                            windSpeedOfGust = details.wind_speed_of_gust ?: 0.0,
                            precipitationAmount = nextHours.next_6_hours.details.precipitation_amount,
                            symbolCode =
                            if (nextHours.next_1_hours != null) nextHours.next_1_hours.summary.symbol_code
                            else nextHours.next_6_hours.summary.symbol_code
                        )
                    } else {
                        null
                    }
                }
            )


        } catch (e: UnknownHostException) {
            null

        }

    }
}

