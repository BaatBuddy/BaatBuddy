package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.autoroute

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData

interface AutorouteRepo {
    suspend fun getAutorouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData?
}

class AutorouteRepository(
    private val dataSource: AutorouteDataSource = AutorouteDataSource()
) : AutorouteRepo {

    override suspend fun getAutorouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData? {
        return dataSource.getAutoRouteData(course, safetyDepth, safetyHeight, boatSpeed)
    }
}