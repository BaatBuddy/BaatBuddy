package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class Details(
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val air_temperature_percentile_10: Double? = null,
    val air_temperature_percentile_90: Double? = null,
    val cloud_area_fraction: Double,
    val cloud_area_fraction_high: Double? = null,
    val cloud_area_fraction_low: Double? = null,
    val cloud_area_fraction_medium: Double? = null,
    val dew_point_temperature: Double? = null,
    val fog_area_fraction: Double? = null,
    val relative_humidity: Double,
    val ultraviolet_index_clear_sky: Double? = null,
    val wind_from_direction: Double,
    val wind_speed: Double,
    val wind_speed_percentile_10: Double? = null,
    val wind_speed_percentile_90: Double? = null,
    val wind_speed_of_gust: Double,
)