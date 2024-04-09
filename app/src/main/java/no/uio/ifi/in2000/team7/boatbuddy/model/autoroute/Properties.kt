package no.uio.ifi.in2000.team7.boatbuddy.model.autoroute

data class Properties(
    val distance: Double,
    val hoursToGo: Double,
    val returnCode: Int,
    val type: String,
    val warnings: List<String>
)