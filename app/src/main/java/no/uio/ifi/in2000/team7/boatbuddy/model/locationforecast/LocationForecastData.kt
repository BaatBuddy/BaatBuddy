package no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast

data class LocationForecastData(
    val lon: Double,
    val lat: Double,
    val timeseries: List<TimeLocationData>
)

data class TimeLocationData(
    val time: String,
    //These are all current/instant values
    val airPressureAtSeaLevel: Double, // luftrykk i hPa
    val airTemperature: Double, //Lufttemperatur 2m over bakken i celsius
    val cloudAreaFraction: Double, // skydekke i prosent
    val fogAreaFraction: Double, // prosent
    val ultravioletIndexClearSky: Double?,
    val relativeHumidity: Double, //Relativ fuktighet 2m over bakken
    val windFromDirection: Double, //Retningen vinden kommer fra (0° er nord, 90° øst, osv.)
    val windSpeed: Double, //Vindhastighet 10m over bakken (10 min gjennomsnitt)
    val windSpeedOfGust: Double, //
    val precipitationAmount: Double, // next 6 hours
    val symbolCode: String,

    )