package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Entity
import com.mapbox.geojson.Point

@Entity(primaryKeys = ["username", "boatname", "routeID"])
data class Route(
    val username: String,
    val boatname: String,
    val routeID: Int,
    val routename: String,
    val route: List<Point>,
    val start: String,
    val finish: String,
)