package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class Awareness(
    val response: String,
    val seriousness: String,
    val level: String,
    val type: String,
    val eventAwarenessName: String
)
