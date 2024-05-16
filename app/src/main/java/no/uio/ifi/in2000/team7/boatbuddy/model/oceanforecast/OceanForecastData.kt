package no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast

data class OceanForecastData(
    val lat: Double,
    val lon: Double,

    val updatedAt: String,

    val timeseries: List<TimeOceanData>

)

data class TimeOceanData(
    val time: String,
    val seaSurfaceWaveFromDirection: Double,
    val seaSurfaceWaveHeight: Double,
    val seaWaterSpeed: Double,
    val seaWaterTemperature: Double,
    val seaWaterToDirection: Double
)


