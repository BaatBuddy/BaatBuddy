package no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeLocationData
import java.net.UnknownHostException

class OceanForecastDataSource(
    private val path: String =
        "https://gw-uio.intark.uh-it.no/in2000/"
) {

    private val client = HttpClient {
        defaultRequest {
            url(path)
            headers.appendIfNameAbsent(
                name = "X-Gravitee-API-Key",
                value = "ea3539d4-efa7-46bd-828d-d05b0c6a86ae"
            )
        }
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun getOceanForecastData(lat: String, lon: String): OceanForecastData? {

        return try {
            // args-order -> lat , lon
            val results = client.get(
                "weatherapi/oceanforecast/2.0/complete?lat=%s&lon=%s".format(
                    lat,
                    lon
                )
            )

            // checks if the api-call is valid
            if (results.status.value !in 200..299) return null

            val data: OceanForecastAPI = results.body()

            val properties = data.properties

            OceanForecastData(
                coordinates = data.geometry.coordinates,
                updated_at = properties.meta.updated_at,
                timeseries = properties.timeseries.map {
                    val details = it.data.instant.details
                    TimeLocationData(
                        time = it.time,
                        sea_surface_wave_from_direction = details.sea_surface_wave_from_direction,
                        sea_surface_wave_height = details.sea_surface_wave_height,
                        sea_water_speed = details.sea_water_speed,
                        sea_water_temperature = details.sea_water_temperature,
                        sea_water_to_direction = details.sea_water_to_direction
                    )
                }
            )

        } catch (e: UnknownHostException) {
            null
        }
    }
}