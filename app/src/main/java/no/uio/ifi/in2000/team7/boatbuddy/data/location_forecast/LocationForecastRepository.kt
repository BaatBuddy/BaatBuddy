package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.dto.LocationForcastCompactDTO

interface LocationForecastRepo {
    suspend fun getLocationForecastData(): LocationForcastCompactDTO?
}

class LocationForecastRepository(private val dataSource: LocationForecastDataSource = LocationForecastDataSource()) :
    LocationForecastRepo {
    override suspend fun getLocationForecastData(): LocationForcastCompactDTO? {
        return dataSource.getLocationForecastData()
    }


}