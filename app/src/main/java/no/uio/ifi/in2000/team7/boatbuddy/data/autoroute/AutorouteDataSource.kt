package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData
import java.net.UnknownHostException

class AutorouteDataSource {

    suspend fun getAutoRouteData(
        course: String,
        //points: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData? {
        var flag = true
        var path: String =
            "https://webapp-no.skippo.io/api/autoroute?usehydrographica=false&"
        var course: String = "course="


        if (course.isNotBlank() && safetyDepth.isNotBlank() && safetyHeight.isNotBlank() && boatSpeed.isNotBlank()) {
            //sette sammen path url


            //forEach Point.fromLngLat
        }

        return try {
            val results = client.get(path)
            val data: AutorouteData = results.body()
            AutorouteData(
                geometry = data.geometry,
                properties = data.properties,
                wayPoints = data.wayPoints
            )


        } catch (e: UnknownHostException) {
            null
        }


    }
}
