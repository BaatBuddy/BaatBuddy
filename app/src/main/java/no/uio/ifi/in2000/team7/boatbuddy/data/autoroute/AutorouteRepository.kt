package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus

interface AutorouteRepo {
    suspend fun getAutorouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): APIStatus
}

class AutorouteRepository(
    private val dataSource: AutorouteDataSource = AutorouteDataSource()
) : AutorouteRepo {

    override suspend fun getAutorouteData(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): APIStatus {
        // check if its valid
        return if (course.size !in 2..10) APIStatus.Failed else dataSource.getAutoRouteData(
            course,
            safetyDepth,
            safetyHeight,
            boatSpeed
        )
    }
}