package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class Data(
    val instant: Instant,
    val next_12_hours: Next12Hours? = null,
    val next_1_hours: Next1Hours? = null,
    val next_6_hours: Next6Hours? = null
)