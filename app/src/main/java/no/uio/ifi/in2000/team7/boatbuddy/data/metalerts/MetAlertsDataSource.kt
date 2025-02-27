package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import java.net.UnknownHostException

class MetAlertsDataSource {

    suspend fun getMetAlertsData(lat: String, lon: String): MetAlertsData? {

        return try {
            var path = "weatherapi/metalerts/2.0/current.json"
            if (lat.isNotBlank() && lon.isNotBlank()) {
                path += "?lat=%s&lon=%s".format(lat, lon)
            }

            val results = client.get(path)

            //checks if API-call is valid
            if (results.status.value !in 200..299) return null

            val data: MetAlertsAPI = results.body()

            // transform data to more usable data
            MetAlertsData(
                lang = data.lang,
                lastChange = data.lastChange,
                features = data.features.map {
                    val properties = it.properties
                    FeatureData(
                        start = it.`when`.interval[0],
                        end = it.`when`.interval[1],
                        awarenessResponse = properties.awarenessResponse,
                        awarenessSeriousness = properties.awarenessSeriousness,
                        eventAwarenessName = properties.eventAwarenessName,
                        awarenessLevel = properties.awareness_level,
                        awarenessType = properties.awareness_type,
                        consequences = properties.consequences,
                        certainty = properties.certainty,
                        geographicDomain = properties.geographicDomain,
                        instruction = properties.instruction,
                        riskMatrixColor = properties.riskMatrixColor,
                        severity = properties.severity,
                        type = properties.type,
                        affectedArea = convertArea(
                            affectedArea = it.geometry.coordinates,
                            areaType = it.geometry.type
                        ),
                        event = properties.event,
                        description = properties.description
                    )
                }
            )

        } catch (
            e: UnknownHostException,
        ) {
            null
        } catch (e: ConnectTimeoutException) {
            null
        }

    }

    // convert metalert special polygon special way of polygons to a more understandable one
    private fun convertArea(
        affectedArea: List<List<List<Any>>>,
        areaType: String,
    ): List<List<List<List<Double>>>> {
        return when (areaType) {
            "MultiPolygon" -> affectedArea.map { area ->
                area.map { polygon ->
                    polygon.map { coordinate ->
                        if (coordinate is List<*>) {
                            coordinate.map { value ->
                                value as Double
                            }
                        } else {
                            listOf(0.0, 0.0)
                        }
                    }
                }
            }

            "Polygon" -> listOf(
                affectedArea.map { area ->
                    area.map { coordinate ->
                        coordinate.map { value ->
                            value as Double
                        }
                    }
                }
            )

            else -> listOf(listOf(listOf(listOf(0.0, 0.0))))
        }
    }
}




