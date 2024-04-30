package no.uio.ifi.in2000.team7.boatbuddy.data.profile

import android.content.Context
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.database.RouteDao
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapBoxCardRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import javax.inject.Inject


class RouteRepository @Inject constructor(
    private val routeDao: RouteDao,
    @ApplicationContext val context: Context
) {


    suspend fun getLastIDUsername(username: String, boatname: String): Int? {
        return routeDao.getLastIDUsernameBoat(username = username, boatname = boatname)
    }

    suspend fun addRouteUsername(
        username: String,
        boatname: String,
        route: List<Point>,
        routename: String
    ) {
        routeDao.insertRoute(
            Route(
                username = username,
                boatname = boatname,
                routeID = getLastIDUsername(username = username, boatname = boatname)?.plus(1) ?: 0,
                routename = routename,
                route = route,
                start = "",
                finish = "",
            )
        )
    }

    suspend fun getAllRoutesUsername(username: String): List<RouteMap> {
        val mapRepo = MapBoxCardRepository()
        return routeDao.getAllRoutesUsername(username = username).map {
            withContext(Dispatchers.Main) {
                
                val mapView = mapRepo.createMap(
                    context = context,
                    points = it.route
                )

                RouteMap(route = it, mapView = mapView)
            }

        }
    }
}