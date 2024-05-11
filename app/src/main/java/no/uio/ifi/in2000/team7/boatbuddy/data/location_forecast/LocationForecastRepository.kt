package no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData

interface LocationForecastRepo {
    suspend fun getLocationForecastData(
        point: Point,
        altitude: String = "0"
    ): LocationForecastData?
}

class LocationForecastRepository(
    private val dataSource: LocationForecastDataSource = LocationForecastDataSource()
) : LocationForecastRepo {
    override suspend fun getLocationForecastData(
        point: Point, altitude: String

    ): LocationForecastData? {
        val lon = point.longitude().toString()
        val lat = point.latitude().toString()
        return dataSource.getLocationForecastData(lon = lon, lat = lat, altitude = altitude)
    }
}