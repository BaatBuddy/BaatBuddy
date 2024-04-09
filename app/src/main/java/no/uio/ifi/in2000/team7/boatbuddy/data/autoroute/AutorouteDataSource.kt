package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import android.util.Log
import com.mapbox.geojson.Point
import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData
import java.net.UnknownHostException

class AutorouteDataSource {

    suspend fun getAutoRouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData? {

        var path: String = ""
        Log.d("Autoroute", "AutorouteDataSource ")




        if (course.isNotEmpty() && safetyDepth.isNotBlank() && safetyHeight.isNotBlank() && boatSpeed.isNotBlank()) {

            var base: String = "https://webapp-no.skippo.io/api/autoroute?usehydrographica=false&"
            var middle: String = "course="
            var end: String = "&safetydepth=5" +
                    "&safetyheight=5" +
                    "&boatspeed=5"

            //TODO: change hardcoded boatspeed depth etc to params
            var flag: Boolean = true


            //temp hardcoded points to be replaced by course parameter
            val liste = listOf<Point>(
                Point.fromLngLat(10.607909077722013, 59.856108702935046),
                Point.fromLngLat(10.607421841197663, 59.860213589239464)
            )

            course.forEach {

                if (flag) {
                    middle += it.longitude().toString()
                    middle += "%2C" + it.latitude().toString()
                    flag = false
                }

                middle += "%3B" + it.longitude().toString()
                middle += "%2C" + it.latitude().toString()


            }

            path = base + middle + end
            Log.d("Autoroute", "Autoroute api call: $path") //Why is this not showing in logcat


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
