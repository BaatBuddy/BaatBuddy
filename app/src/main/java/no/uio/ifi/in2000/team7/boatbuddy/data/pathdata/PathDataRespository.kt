package no.uio.ifi.in2000.team7.boatbuddy.data.pathdata

import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastDataSource
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastDataSource
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseDataSource


interface PathDataRepo {
    // Any is set to change to a data class that represents the data per point
    fun getDataPerPoint(lat: Double, lon: Double): Any
    fun getDataPerPath(points: List<Pair<Double, Double>>): List<Any>
}

class PathDataRepository(
    private val oceanData: OceanForecastDataSource = OceanForecastDataSource(),
    private val locationData: LocationForecastDataSource = LocationForecastDataSource(),
    private val sunriseData: SunriseDataSource = SunriseDataSource()

) : PathDataRepo {

    override fun getDataPerPoint(lat: Double, lon: Double): Any {
        return 1
    }

    override fun getDataPerPath(points: List<Pair<Double, Double>>): List<Any> {
        return listOf()
    }

}
