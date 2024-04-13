package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData

class WeatherCalculatorRepository {
    val oceanForecastRepository = OceanForecastRepository()
    val locationForecastRepository = LocationForecastRepository()


    suspend fun fetchPathWeather(points: List<Point>): List<PathWeatherData?> {
        return points.map {
            val lat = it.latitude().toString()
            val lon = it.longitude().toString()

            val oceanData = oceanForecastRepository.getOceanForecastData(
                lat = lat,
                lon = lon,
            )
            val locationData = locationForecastRepository.getLocationForecastData(
                lat = lat,
                lon = lon
            )

            locationData?.timeseries?.mapNotNull { ld ->
                // finds corresponding ocean data
                val od = oceanData?.timeseries?.firstOrNull { tod ->
                    tod.time == ld.time
                }

                if (od != null) {
                    TimeWeatherData(
                        time = ld.time,
                        waveHeight = od.sea_surface_wave_height,
                        waterTemperature = od.sea_water_temperature,
                        windSpeed = ld.wind_speed,
                        airTemperature = ld.air_temperature,
                        cloudAreaFraction = ld.cloud_area_fraction,
                        fogAreaFraction = ld.fog_area_fraction,
                        relativeHumidity = ld.relative_humidity
                    )
                } else {
                    null
                }
            }?.let { it1 ->
                PathWeatherData(
                    lat = lat.toDouble(),
                    lon = lon.toDouble(),
                    timeWeatherData =
                    it1
                )
            }
        }
    }

}