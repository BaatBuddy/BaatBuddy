package no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast

data class OceanForecastData(
    val lat: Double,
    val lon: Double,

    val updated_at: String,

    val timeseries: List<TimeOceanData>

)

data class TimeOceanData(
    val time: String,
    val sea_surface_wave_from_direction: Double,
    val sea_surface_wave_height: Double,
    val sea_water_speed: Double,
    val sea_water_temperature: Double,
    val sea_water_to_direction: Double
)


