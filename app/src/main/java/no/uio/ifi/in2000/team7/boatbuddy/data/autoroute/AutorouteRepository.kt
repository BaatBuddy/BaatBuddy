package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData

interface AutorouteRepo {
    suspend fun getAutorouteData(
        course: String,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData?
}

class AutorouteRepository(
    private val dataSource: AutorouteDataSource = AutorouteDataSource()
) : AutorouteRepo {

    override suspend fun getAutorouteData(
        course: String,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ): AutorouteData? {
        return dataSource.getAutoRouteData(course, safetyDepth, safetyHeight, boatSpeed)
    }
}