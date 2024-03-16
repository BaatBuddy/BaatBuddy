package no.uio.ifi.in2000.team7.boatbuddy.data.sunrise

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.team7.boatbuddy.data.APIClient.client
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseAPI
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData
import java.net.UnknownHostException

class SunriseDataSource {


    // eventually use this as a test program as one of the ten
    private fun validInput(lat: String, lon: String, date: String): Boolean {
        return true
        /* // checks if lat and lon is doubles and inside the -90 to 90 range
        // must be > and not >=
        try {
            val latDouble = lat.toDouble()
            val lonDouble = lon.toDouble()
            if (-90 < max(latDouble, lonDouble) && max(latDouble, lonDouble) < 90) return false
        } catch (e: NumberFormatException) {
            return false
        }

        // checks for a valid date (format: YYYY-MM-DD)
        val slicedDate: List<String> = date.split("-")
        return !(booleanArrayOf(
            slicedDate.size != 3,
            slicedDate[0].length != 4,
            slicedDate[1].length != 2,
            slicedDate[2].length != 2,
            slicedDate.any { it.isDigitsOnly() }
        ).any())*/
    }

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
                coordinates = data.geometry.coordinates,
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