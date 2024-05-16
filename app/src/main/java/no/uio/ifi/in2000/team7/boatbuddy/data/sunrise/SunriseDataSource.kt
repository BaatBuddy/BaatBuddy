package no.uio.ifi.in2000.team7.boatbuddy.data.sunrise

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData
import java.net.UnknownHostException

class SunriseDataSource {

    suspend fun getSunriseData(lat: String, lon: String, date: String): SunriseData? {

        var url: String = "weatherapi/sunrise/3.0/sun?lat=%s&lon=%s".format(lat, lon)
        if (date.isNotBlank()) {
            url += "&date=%s&offset=+01:00".format(date)
        }

        return try {
            // args-order -> lat , lon , date ("YYYY-MM-DD")
            val results = client.get(url)

            // checks if the api-call is valid
            if (results.status.value !in 200..299) return null

            val data: SunriseAPI = results.body()

            val properties = data.properties

            SunriseData(
                lon = data.geometry.coordinates[0],
                lat = data.geometry.coordinates[1],
                interval = data.`when`.interval,

                sunriseTime = properties.sunrise.time,
                sunriseAzimuth = properties.sunrise.azimuth,

                sunsetTime = properties.sunset.time,
                sunsetAzimuth = properties.sunset.azimuth,

                solarnoonTime = properties.solarnoon.time,
                solarnoonElevation = properties.solarnoon.disc_centre_elevation,
                solarnoonVisible = properties.solarnoon.visible,

                solarmidnightTime = properties.solarmidnight.time,
                solarmidnightElevation = properties.solarmidnight.disc_centre_elevation,
                solarmidnightVisible = properties.solarmidnight.visible
            )

        } catch (e: UnknownHostException) {
            null
        }
    }
}