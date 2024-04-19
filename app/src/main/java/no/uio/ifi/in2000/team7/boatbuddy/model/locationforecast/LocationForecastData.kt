package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class LocationForecastData(
    val lon: Double,
    val lat: Double,
    val timeseries: List<TimeLocationData>
)

data class TimeLocationData(
    val time: String,
    //These are all current/instant values
    val air_pressure_at_sea_level: Double, // luftrykk i hPa
    val air_temperature: Double, //Lufttemperatur 2m over bakken i celsius
    val cloud_area_fraction: Double, // skydekke i prosent
    val fog_area_fraction: Double, // prosent
    val ultraviolet_index_clear_sky: Double?,
    val relative_humidity: Double, //Relativ fuktighet 2m over bakken
    val wind_from_direction: Double, //Retningen vinden kommer fra (0° er nord, 90° øst, osv.)
    val wind_speed: Double, //Vindhastighet 10m over bakken (10 min gjennomsnitt)
    val wind_speed_of_gust: Double, //
    val precipitation_amount: Double, // next 6 hours
    val symbol_code: String,

    )