package no.uio.ifi.in2000.team7.boatbuddy.model.route

import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route

data class RouteMap(
    val route: Route,
    val mapURL: String,
)