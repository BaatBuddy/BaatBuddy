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
    val awareness_level: String,
    val awareness_type: String,
    val certainty: String,
    val geographicDomain: String,
    val instruction: String,
    val riskMatrixColor: String,
    val severity: String,
    val type: String,
    val affected_area : List<List<List<Any>>>

)



    /*posisjon, farevarsel, vurdering osv.
    * val coordinates: List<List<List<List<Any>>>>,*/

