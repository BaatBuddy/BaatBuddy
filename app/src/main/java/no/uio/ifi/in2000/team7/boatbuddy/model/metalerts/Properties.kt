package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class Properties (
    val area: String,
    val awareness: Awareness,
    val certainty: String,
    val consequences: String,
    val county: List<Any>,
    val description: String,
    val event: String,
    val geographic: Geographic,
    val id: String,
    val instruction: String,
    val resources: List<Resource>,
    val risk: Risk,
    val severity: String,
    val title: String,
    val trigger: Trigger,
)
