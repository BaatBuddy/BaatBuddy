package no.uio.ifi.in2000.team7.boatbuddy.model.route

import com.mapbox.maps.MapView
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository

data class RouteMap(
    val route: Route,
    val mapView: MapView,
)