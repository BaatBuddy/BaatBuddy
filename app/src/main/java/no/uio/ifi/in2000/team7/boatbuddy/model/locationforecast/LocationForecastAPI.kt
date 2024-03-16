package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class LocationForecastAPI(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)