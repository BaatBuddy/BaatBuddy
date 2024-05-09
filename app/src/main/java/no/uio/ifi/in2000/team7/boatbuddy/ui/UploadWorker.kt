package no.uio.ifi.in2000.team7.boatbuddy.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService.enqueueWork
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository

@HiltWorker
class UpdateDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val metAlertsRepository: MetAlertsRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        metAlertsRepository.updateAlertData()
        Log.i("ASDASD", "work")
        return Result.retry()
    }
}