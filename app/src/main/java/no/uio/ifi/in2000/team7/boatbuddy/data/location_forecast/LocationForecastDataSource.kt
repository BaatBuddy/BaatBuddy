package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForcastCompactDTO
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import java.net.UnknownHostException

class LocationForecastDataSource(private val path: String = "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact?lat=59.93&lon=10.72&altitude=90") {
    // https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact?lat=59.93&lon=10.72&altitude=90
    private val client = HttpClient() {
        defaultRequest {
            url(path)
            headers.appendIfNameAbsent("X-Gravitee-API-Key", "ea3539d4-efa7-46bd-828d-d05b0c6a86ae")
        }
        install(ContentNegotiation) {
            gson()

        }
    }

    //Tar imot Lat, lon og altitude
    suspend fun getLocationForecastData(
        lat: String,
        lon: String,
        altitude: String
    ): LocationForecastData? {

        val pathLon =
            "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact?lat=%s&lon=%s&altitude=%s"
        return try {
            //order of args -> lat, lon, altitude
            val result: LocationForcastCompactDTO =
                client.get(pathLon.format(lat, lon, altitude)).body()

            LocationForecastData(
                air_pressure_at_sea_level = result.properties.timeseries.get(0).data.instant.details.air_pressure_at_sea_level,
                air_temperature = result.properties.timeseries.get(0).data.instant.details.air_temperature,
                cloud_area_fraction = result.properties.timeseries.get(0).data.instant.details.cloud_area_fraction,
                relative_humidity = result.properties.timeseries.get(0).data.instant.details.relative_humidity,
                wind_from_direction = result.properties.timeseries.get(0).data.instant.details.wind_from_direction,
                wind_speed = result.properties.timeseries.get(0).data.instant.details.wind_speed
            )


        } catch (e: UnknownHostException) {
            null
        }

    }
}