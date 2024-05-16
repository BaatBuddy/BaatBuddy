package no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData
import java.net.UnknownHostException

class OceanForecastDataSource {

    suspend fun getOceanForecastData(lat: String, lon: String): OceanForecastData? {

        return try {
            var path = "weatherapi/oceanforecast/2.0/complete"
            if (lat.isNotBlank() && lon.isNotBlank()) {
                path += "?lat=%s&lon=%s".format(lat, lon)
            }
            val results = client.get(path)

            // checks if the api-call is valid
            if (results.status.value !in 200..299) return null

            val data: OceanForecastAPI = results.body()

            val properties = data.properties

            OceanForecastData(
                lat = data.geometry.coordinates[1],
                lon = data.geometry.coordinates[0],
                updatedAt = properties.meta.updated_at,
                timeseries = properties.timeseries.map {
                    val details = it.data.instant.details
                    TimeOceanData(
                        time = it.time,
                        seaSurfaceWaveFromDirection = details.sea_surface_wave_from_direction,
                        seaSurfaceWaveHeight = details.sea_surface_wave_height,
                        seaWaterSpeed = details.sea_water_speed,
                        seaWaterTemperature = details.sea_water_temperature,
                        seaWaterToDirection = details.sea_water_to_direction
                    )
                }
            )

        } catch (e: UnknownHostException) {
            null
        }
    }
}