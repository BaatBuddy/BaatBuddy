package no.uio.ifi.in2000.team7.boatbuddy.data.sunrise

import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData

interface SunriseRepo {
    suspend fun getSunriseData(lat: String, lon: String, date: String) : SunriseData?
}

class SunriseRepository(
    private val dataSource: SunriseDataSource = SunriseDataSource()
) : SunriseRepo {

    override suspend fun getSunriseData(lat: String, lon: String, date: String): SunriseData? {
        return dataSource.getSunriseData(lat, lon, date)
    }



}
