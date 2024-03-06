package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)
