package no.uio.ifi.in2000.team7.boatbuddy.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent


object APIClient {
    val client = HttpClient {
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            val variables = System.getenv()
            headers.appendIfNameAbsent(
                name = "X-Gravitee-API-Key",
                value = variables["APIKEY"] ?: ""
            )
        }
        install(ContentNegotiation) {
            gson()
        }
    }
}