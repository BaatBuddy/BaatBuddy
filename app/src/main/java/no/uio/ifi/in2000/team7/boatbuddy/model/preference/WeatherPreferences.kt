package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class WeatherPreferences(
    val waveHeight: Double = 0.5, // default value
    val windSpeed: Double, // 0 to 12m/s
    val airTemperature: Double, // 15 to 30C
    val cloudAreaFraction: Double, // 0 to 100%
    val waterTemperature: Double?, // user picked 15 to 25C
    val relativeHumidity: Double?, // user picked 0 to 100%
    val precipitationAmount: Double = 0.0, // 0 out of 100
    val fogAreaFraction: Double = 0.0 // 0 out of 100
)