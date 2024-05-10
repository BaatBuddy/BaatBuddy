package no.uio.ifi.in2000.team7.boatbuddy.data.location

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import no.uio.ifi.in2000.team7.boatbuddy.data.location.userlocation.UserLocationRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository

@HiltWorker
class UpdateDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val metAlertsRepository: MetAlertsRepository,
    private val sunriseRepository: SunriseRepository,
    private val userLocationRepository: UserLocationRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val userLocation = userLocationRepository.fetchUserLocation()
        if (userLocation != null) {
            metAlertsRepository.updateAlertData()
            sunriseRepository.updateSunriseData(userLocation)
            Log.i("ASDASD", "HAR LOKASJON")
        } else {
            Log.i("ASDASD", "HAR IKKE LOKASJON?")
            return Result.retry()
        }
        Log.i("ASDASD", "WORK BITCH WORK!")

        return Result.success()
    }
}