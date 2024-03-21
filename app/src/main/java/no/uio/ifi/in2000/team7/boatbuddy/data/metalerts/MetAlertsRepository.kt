package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData


interface MetAlertsRepo {
    suspend fun getMetAlertsData(lat: String, lon: String) : MetAlertsData?
}

class MetAlertsRepository(private val dataSource: MetAlertsDataSource = MetAlertsDataSource()) : MetAlertsRepo {

        override suspend fun getMetAlertsData(lat: String, lon: String): MetAlertsData? {
            return dataSource.getMetAlertsData(lat, lon)
        }


}
