package no.uio.ifi.in2000.team7.boatbuddy.data.sunrise

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.sunsetToday
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData

interface SunriseRepo {
    suspend fun getSunriseData(lat: String, lon: String, date: String): SunriseData?
}

class SunriseRepository(
    private val dataSource: SunriseDataSource = SunriseDataSource(),
) : SunriseRepo {

    override suspend fun getSunriseData(lat: String, lon: String, date: String): SunriseData? {
        return dataSource.getSunriseData(lat, lon, date)
    }

    // updates cache
    suspend fun updateSunriseData(point: Point) {
        val lat = point.latitude().toString()
        val lon = point.longitude().toString()

        val sunData = dataSource.getSunriseData(lat = lat, lon = lon, date = "")
        if (sunData != null) {
            sunsetToday = sunData.sunsetTime ?: ""
        }

    }


}
