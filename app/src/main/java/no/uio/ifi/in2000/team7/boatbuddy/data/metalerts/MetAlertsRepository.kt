package no.uio.ifi.in2000.team7.boatbuddy.data.metalerts

import coil.decode.DataSource
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData

interface MetAlertsRepo {
    suspend fun getMetAlertsData() : MetAlertsData?
}

class MetAlertsRepository(private val dataSource: MetAlertsDataSource = MetAlertsDataSource()) : MetAlertsRepo {

    override suspend fun getMetAlertsData(): MetAlertsData? {
        return dataSource.getMetAlertsData()
    }


}