package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import java.net.UnknownHostException

class MetAlertsDataSource(private val path: String = "https://api.met.no/weatherapi/metalerts/2.0/current.json") {
    private val client = HttpClient{
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun getMetAlertsData() : MetAlertsData? {

        return try {
            val result: MetAlertsData = client.get(path).body()
            result
        } catch (
            e: UnknownHostException
        ) {
            null
        }
    }
}