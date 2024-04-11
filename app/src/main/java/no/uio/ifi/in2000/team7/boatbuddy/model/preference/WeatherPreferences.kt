package no.uio.ifi.in2000.team7.boatbuddy.model.preference

data class WeatherPreferences(
    val waveHeight: FactorPreference,
    val waterTemperature: FactorPreference,
    val windSpeed: FactorPreference,
    // val windSpeedOfGust: FactorPreference,
    val airTemperature: FactorPreference,
    val cloudAreaFraction: FactorPreference,
    val fogAreaFraction: FactorPreference,
    // val ultravioletIndexClearSky: FactorPreference,
    val relativeHumidity: FactorPreference,
)