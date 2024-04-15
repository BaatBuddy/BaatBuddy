package no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.PathWeatherData
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData

class WeatherCalculatorRepository {
    val oceanForecastRepository = OceanForecastRepository()
    val locationForecastRepository = LocationForecastRepository()
    val sunriseRepository = SunriseRepository()


    suspend fun fetchPathWeatherData(points: List<Point>): List<PathWeatherData> {
        return points.mapNotNull { point ->
            val lat = point.latitude().toString()
            val lon = point.longitude().toString()

            val oceanData = oceanForecastRepository.getOceanForecastData(
                lat = lat,
                lon = lon,
            )
            val locationData = locationForecastRepository.getLocationForecastData(
                lat = lat,
                lon = lon,
            )

            locationData?.timeseries?.mapNotNull { ld ->
                // finds corresponding ocean data
                val od = oceanData?.timeseries?.firstOrNull { tod ->
                    tod.time == ld.time
                }

                if (od != null) {
                    TimeWeatherData(
                        lat = lat.toDouble(),
                        lon = lon.toDouble(),
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
            }
        }.flatten().groupBy { twd ->
            twd.time.substring(0, 10)
        }.map {
            val lastPoint = points.last()
            PathWeatherData(
                date = it.key,
                sunsetAtLastPoint = sunriseRepository.getSunriseData(
                    lastPoint.latitude().toString(), lastPoint.longitude().toString(), it.key
                )?.sunsetTime,
                timeWeatherData = it.value
            )
        }
    }
}