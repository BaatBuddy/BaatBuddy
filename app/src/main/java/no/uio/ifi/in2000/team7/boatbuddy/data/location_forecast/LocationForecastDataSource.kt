package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.dto.LocationForcastCompactDTO
import java.net.UnknownHostException

class LocationForecastDataSource(private val path: String = "url") {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }


    suspend fun getLocationForecastData(): LocationForcastCompactDTO? {

        return try {
            val result: LocationForcastCompactDTO = client.get(path).body()
            result
        } catch (e: UnknownHostException) {
            null
        }
    }
}