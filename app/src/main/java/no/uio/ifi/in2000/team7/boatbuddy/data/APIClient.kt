package no.uio.ifi.in2000.team7.boatbuddy.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent


object APIClient {
    // default client for all MET API
    // uses uio proxy
    val client = HttpClient {
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            headers.appendIfNameAbsent(
                name = "X-Gravitee-API-Key",
                value = "ea3539d4-efa7-46bd-828d-d05b0c6a86ae"
            )
        }
        install(ContentNegotiation) {
            gson()
        }
    }
}