package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import java.net.UnknownHostException

class LocationForecastDataSource {
    // https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact?lat=59.93&lon=10.72&altitude=90


    //Tar imot Lat, lon og altitude
    suspend fun getLocationForecastData(
        lat: String,
        lon: String,
        altitude: String
    ): LocationForecastData? {


        var path = "weatherapi/locationforecast/2.0/compact"

        if (lat.isNotBlank() && lon.isNotBlank()) {
            path += "?lat=%s&lon=%s".format(lat, lon)
            if (altitude.isNotBlank()) {
                path += "&altitude=%s".format(altitude)
            }
        }

        return try {
            //order of args -> lat, lon, altitude
            val results = client.get(path)
            if (results.status.value !in 200..299) return null

            val data: LocationForecastAPI = results.body()

            val coordinates = data.geometry.coordinates
            val timeseries = data.properties.timeseries

            LocationForecastData(
                lat = coordinates[1],
                lon = coordinates[0],
                timeseries = timeseries.map { timesery ->
                    val details = timesery.data.instant.details
                    TimeLocationData(
                        time = timesery.time,
                        air_pressure_at_sea_level = details.air_pressure_at_sea_level,
                        air_temperature = details.air_temperature,
                        cloud_area_fraction = details.cloud_area_fraction,
                        fog_area_fraction = details.fog_area_fraction,
                        ultraviolet_index_clear_sky = details.ultraviolet_index_clear_sky,
                        relative_humidity = details.relative_humidity,
                        wind_from_direction = details.wind_from_direction,
                        wind_speed = details.wind_speed,
                        wind_speed_of_gust = details.wind_speed_of_gust
                    )
                }
            )


        } catch (e: UnknownHostException) {
            null

        }

    }
}