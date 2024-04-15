package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class DateScore(
    val date: String,
    val score: Double,
    val isDoneBeforeSunset: Boolean,
)
