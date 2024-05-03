package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RouteDao {
    @Upsert
    suspend fun insertRoute(route: Route)

    @Delete
    suspend fun deleteRoute(route: Route)

    @Query("SELECT * FROM route as r INNER JOIN userprofile as u ON r.username = u.username WHERE u.username LIKE :username")
    suspend fun getAllRoutesUsername(username: String): List<Route>

    @Query("SELECT MAX(r.routeID) FROM route as r INNER JOIN boatprofile as b ON r.username = b.username WHERE b.username = :username AND b.boatname = :boatname")
    suspend fun getLastIDUsernameBoat(username: String, boatname: String): Int?
}
