package no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast

import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastData

interface OceanForecastRepo {
    suspend fun getOceanForecastData(lat: String, lon: String): OceanForecastData?
}

class OceanForecastRepository(
    private val dataSource: OceanForecastDataSource = OceanForecastDataSource(),
) : OceanForecastRepo {

    // fetch ocean data
    override suspend fun getOceanForecastData(
        lat: String,
        lon: String,
    ): OceanForecastData? {
        return dataSource.getOceanForecastData(lat, lon)
    }


}
