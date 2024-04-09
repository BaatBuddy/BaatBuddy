package no.uio.ifi.in2000.team7.boatbuddy.model.autoroute

data class AutorouteData(
    val geometry: Geometry,
    val properties: Properties,
    val wayPoints: List<Int>
)