package no.uio.ifi.in2000.team7.boatbuddy.model.sunrise

data class Properties(
    val body: String,
    val solarmidnight: Solarmidnight,
    val solarnoon: Solarnoon,
    val sunrise: Sunrise,
    val sunset: Sunset
)