package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData

interface LocationForecastRepo {
    suspend fun getLocationForecastData(
        lat: String,
        lon: String,
        altitude: String
    ): LocationForecastData?
}

class LocationForecastRepository(private val dataSource: LocationForecastDataSource = LocationForecastDataSource()) :
    LocationForecastRepo {
    override suspend fun getLocationForecastData(
        lat: String, lon: String, altitude: String

    ): LocationForecastData? {
        return dataSource.getLocationForecastData(lat, lon, altitude)
    }


}