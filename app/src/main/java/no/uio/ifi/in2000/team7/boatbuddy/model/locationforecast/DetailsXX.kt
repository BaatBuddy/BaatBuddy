package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class DetailsXX(
    val precipitation_amount: Double,
    val precipitation_amount_max: Double? = null,
    val precipitation_amount_min: Double? = null,
    val probability_of_precipitation: Double? = null,
    val probability_of_thunder: Double? = null
)