package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import java.net.UnknownHostException

class MetAlertsDataSource(
    private val path: String =  "https://gw-uio.intark.uh-it.no/in2000/") {


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

    suspend fun getMetAlertsData(): MetAlertsData? {
        return try {
            val result: MetAlertsData = client.get(path).body()
            result
        } catch (
            e: UnknownHostException
        ) {
            null
        }
        suspend fun getMetAlertsData(): MetAlertsData? {


            return try {

                val results = client.get("weatherapi/metalerts/2.0/current.json")

                //checks if API-call is valid
                if (results.status.value !in 200..299) return null

                val data: MetAlertsAPI = results.body()

                Log.d("SSSS", data.toString())


                MetAlertsData(
                    lang = data.lang,
                    lastChange = data.lastChange,
                    features = data.features.map {
                        val properties = it.properties
                        FeatureData(
                            start = it.`when`.interval[0],
                            end = it.`when`.interval[1],
                            awarenessResponse = properties.awarenessResponse,
                            awareness_level = properties.awareness_level,
                            awareness_type = properties.awareness_type,
                            certainty = properties.certainty,
                            geographicDomain = properties.geographicDomain,
                            instruction = properties.instruction,
                            riskMatrixColor = properties.riskMatrixColor,
                            severity = properties.severity,
                            type = properties.type,
                            affected_area = it.geometry.coordinates
                        )
                    }
                )

            } catch (
                e: UnknownHostException
            ) {
                null
            }

        }
    }
}


