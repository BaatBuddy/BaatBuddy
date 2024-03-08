package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.dto

data class LocationForcastCompactDTO(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)