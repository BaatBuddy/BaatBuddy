package no.uio.ifi.in2000.team7.boatbuddy.model.sunrise

data class SunriseAPI(
    val copyright: String,
    val geometry: Geometry,
    val licenseURL: String,
    val properties: Properties,
    val type: String,
    val `when`: When
)