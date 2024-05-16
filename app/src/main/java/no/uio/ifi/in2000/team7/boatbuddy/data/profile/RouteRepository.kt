package no.uio.ifi.in2000.team7.boatbuddy.data.profile

import android.content.Context
import android.util.Log
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.database.RouteDao
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.finishTime
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.points
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.sdf
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.startTime
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import java.util.Date
import javax.inject.Inject


class RouteRepository @Inject constructor(
    private val routeDao: RouteDao,
    @ApplicationContext val context: Context,
    private val mapRepo: MapboxRepository
) {


    private suspend fun getLastIDUsername(username: String, boatname: String): Int? {
        return routeDao.getLastIDUsernameBoat(username = username, boatname = boatname)
    }

    suspend fun addRouteUsername(
        username: String,
        boatname: String,
        routename: String,
        routeDescription: String,
        route: List<Point>? = null,
    ) {
        val routeID = getLastIDUsername(username = username, boatname = boatname)?.plus(1) ?: 0

        routeDao.insertRoute(
            Route(
                username = username,
                boatname = boatname,
                routeID = routeID,
                routename = routename.ifBlank { "Rute $routeID" },
                routeDescription = routeDescription,
                route = route ?: points,
                start = startTime,
                finish = finishTime,
            )
        )
        if (route == null) {
            points = mutableListOf()
        }
    }

    suspend fun getAllRoutesUsername(username: String): List<RouteMap> {

        return routeDao.getAllRoutesUsername(username = username).map {
            withContext(Dispatchers.Main) {
                Log.i("ASDASD", it.route.toString())
                val mapURL = mapRepo.generateMapURI(it.route)
                RouteMap(route = it, mapURL = mapURL)
            }

        }
    }

    fun getStartTime(): String {
        return startTime
    }

    fun getFinishTime(): String {
        return finishTime
    }

    fun setFinalFinishTime() {
        finishTime = sdf.format(Date())
    }

    suspend fun getTemporaryRouteView(points: List<Point>?): String {
        return mapRepo.generateMapURI(points = points ?: AlertNotificationCache.points)
    }

    suspend fun deleteRoute(routeMap: RouteMap) {
        routeDao.deleteRoute(routeMap.route)
    }
}