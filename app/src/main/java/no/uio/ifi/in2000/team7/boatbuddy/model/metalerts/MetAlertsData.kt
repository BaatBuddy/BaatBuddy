package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

data class MetAlertsData(
    val lang: String,
    val lastChange: String,
    val features: List<FeatureData>,
)

data class FeatureData(
    // intervall
    val start: String,
    val end: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String, // LAGT TIL
    val eventAwarenessName: String, // LAGT TIL
    val description: String, // LAGT TIL
    val awareness_level: String,
    val awareness_type: String,
    val consequences: String,
    val certainty: String,
    val geographicDomain: String?,
    val instruction: String,
    val riskMatrixColor: String,
    val severity: String,
    val type: String, // consider removing (always "Feature")
    val event: String,
    val affected_area: List<List<List<List<Double>>>>

)


/*posisjon, farevarsel, vurdering osv.
* val coordinates: List<List<List<List<Any>>>>,*/

