package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import android.util.Log
import com.mapbox.geojson.Point
import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData
import java.io.IOException
import java.net.UnknownHostException

class AutorouteDataSource {

    suspend fun getAutoRouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): APIStatus {

        var path: String = ""
        Log.d("Autoroute", "AutorouteDataSource ")

        if (course.isNotEmpty() && safetyDepth.isNotBlank() && safetyHeight.isNotBlank() && boatSpeed.isNotBlank()) {

            val base: String = "https://webapp-no.skippo.io/api/autoroute?usehydrographica=false&"
            var middle: String = "course="
            val end: String = "&safetydepth=5" +
                    "&safetyheight=5" +
                    "&boatspeed=5"

            //TODO: change hardcoded boatspeed depth etc to params
            var flag: Boolean = true


            //temp hardcoded points to be replaced by course parameter
            val liste = listOf<Point>(
                Point.fromLngLat(10.707517, 59.879888),
                Point.fromLngLat(10.251066, 59.736283)
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
            //Log.d("Autoroute", "Autoroute api call: $path") //Why is this not showing in logcat
        }

        return try {
            val results = client.get(path)
            Log.d("Autoroute", "Autoroute api call")


            if (results.status.value in 500..599) return APIStatus.Failed

            val data: AutorouteData = results.body()

            APIStatus.Success(data)


        } catch (e: UnknownHostException) {
            APIStatus.Failed
        } catch (e: IOException) {  // Handle network issues
            Log.d("Autoroute", "Network error: ${e.message}")
            APIStatus.Failed
        }

    }
}
