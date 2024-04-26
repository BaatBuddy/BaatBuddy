package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class TimeWeatherData(
    val lat: Double,
    val lon: Double,
    val time: String,
    val waveHeight: Double,
    val waterTemperature: Double,
    val windSpeed: Double,
    val windSpeedOfGust: Double,
    val airTemperature: Double,
    val cloudAreaFraction: Double,
    val fogAreaFraction: Double,
    // val ultravioletIndexClearSky: Double?,
    val relativeHumidity: Double,
    val precipitationAmount: Double,
    val symbolCode: String,
)
