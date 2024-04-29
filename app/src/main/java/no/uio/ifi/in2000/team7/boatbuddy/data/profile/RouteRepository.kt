package no.uio.ifi.in2000.team7.boatbuddy.data.profile

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.database.RouteDao
import javax.inject.Inject


class RouteRepository @Inject constructor(
    val routeDao: RouteDao
) {


    suspend fun getLastIDUsername(username: String, boatname: String): Int? {
        return routeDao.getLastIDUsernameBoat(username = username, boatname = boatname)
    }

    suspend fun addRouteUsername(username: String, boatname: String, route: List<Point>) {
        routeDao.insertRoute(
            Route(
                username = username,
                boatname = boatname,
                routeID = getLastIDUsername(username = username, boatname = boatname)?.plus(1) ?: 0,
                route = route,
                start = "",
                finish = "",
            )
        )
    }
}