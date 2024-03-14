package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class LocationForecastData(
    //These are all current/instant values
    val air_pressure_at_sea_level: Double, //Lufttrykk på havnivå
    val air_temperature: Double, //Lufttemperatur 2m over bakken
    val cloud_area_fraction: Double, //Total skydekke for alle høyder
    val relative_humidity: Double, //Relativ fuktighet 2m over bakken
    val wind_from_direction: Double, //Retningen vinden kommer fra (0° er nord, 90° øst, osv.)
    val wind_speed: Double //Vindhastighet 10m over bakken (10 min gjennomsnitt)
)