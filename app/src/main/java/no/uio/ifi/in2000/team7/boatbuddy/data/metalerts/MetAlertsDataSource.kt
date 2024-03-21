package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import java.net.UnknownHostException

class MetAlertsDataSource {

    suspend fun getMetAlertsData(lat: String, lon: String): MetAlertsData? {


        return try {
            var path: String = "weatherapi/metalerts/2.0/current.json"
            if (lat.isNotBlank() && lon.isNotBlank()) {
                path += "?lat=%s&lon=%s".format(lat, lon)
            }

            val results = client.get(path)

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
                        affected_area = it.geometry.coordinates,
                        consequences = properties.consequences
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




