package no.uio.ifi.in2000.team7.boatbuddy.data.autoroute

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.database.BoatProfileDao
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import javax.inject.Inject

interface AutorouteRepo {
    suspend fun getAutorouteData(
        course: List<Point>,
    ): APIStatus
}

class AutorouteRepository @Inject constructor(
    private val dataSource: AutorouteDataSource,
    private val boatDao: BoatProfileDao,
) : AutorouteRepo {

    override suspend fun getAutorouteData(
        course: List<Point>,
    ): APIStatus {

        // get selected user boatsize else use default
        val boat = boatDao.getSelectedBoatSelectedUser()

        // check if its valid
        return if (course.size !in 2..10) APIStatus.Failed else dataSource.getAutoRouteData(
            course,
            boat?.safetyDepth ?: "5",
            boat?.safetyHeight ?: "5",
            boat?.boatSpeed ?: "10"
        )
    }
}