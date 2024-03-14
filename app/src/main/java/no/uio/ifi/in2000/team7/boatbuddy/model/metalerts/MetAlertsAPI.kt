package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class MetAlertsAPI (
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)