package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class Properties (
    val altitude_above_sea_level: Int,
    val area: String,
    val awarenessResponse: String,
    val awareness_level: String,
    val awareness_type: String,
    val ceiling_above_sea_level: Int,
    val certainty: String,
    val contact: String,
    val county: List<Any>,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val eventEndingTime: String,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val resources: List<Resource>,
    val riskMatrixColor: String,
    val severity: String,
    val status: String,
    val title: String,
    val type: String,
    val web: String

)
