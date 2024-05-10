package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import android.util.Log
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.PolygonPosition
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData


interface MetAlertsRepo {
    suspend fun getMetAlertsData(lat: String, lon: String): MetAlertsData?
}

class MetAlertsRepository(
    private val dataSource: MetAlertsDataSource = MetAlertsDataSource()
) : MetAlertsRepo {

    // every alerts
    private var metalertData: MetAlertsData? = null

    override suspend fun getMetAlertsData(lat: String, lon: String): MetAlertsData? {
        if (lat + lon != "") {
            metalertData = dataSource.getMetAlertsData(lat, lon)
        } else {
            return dataSource.getMetAlertsData(lat, lon)
        }
        return metalertData
    }

    suspend fun getAlertsForPoints(points: List<Point>): List<FeatureData> {
        val fds = mutableSetOf<FeatureData>()
        metalertData = dataSource.getMetAlertsData("", "")
        points.forEach {
            metalertData?.let { it1 ->
                PolygonPosition.checkUserLocationAlertAreas(
                    lon = it.longitude(),
                    lat = it.latitude(),
                    featureData = it1.features
                )
            }?.let { it2 -> fds.addAll(it2) }
        }
        Log.i("ASDASD", fds.toString())
        return fds.toList()
    }


}
