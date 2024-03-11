package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class Properties (
    val area: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awareness_level: String,
    val awareness_type: String,
    val certainty: String,
    val consequences: String,
    val county: List<Any>,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val resources: List<Resource>,
    val riskMatrixColor: String,
    val severity: String,
    val title: String,
    val triggerLevel: String,
    val type: String

)
