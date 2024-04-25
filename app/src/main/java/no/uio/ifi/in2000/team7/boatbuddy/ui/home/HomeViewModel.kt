package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val showNotificationDialog = mutableStateOf(false)

    private val preferences =
        getApplication<Application>().getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch {
            val isFirstStart = preferences.getBoolean("firstStart", true)
            if (isFirstStart) {
                showNotificationDialog.value = true
                with(preferences.edit()) {
                    putBoolean("firstStart", false)
                    apply()
                }
            }
        }
    }

    fun navigateToNotificationSettings() {
        val intent = Intent().apply {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, getApplication<Application>().packageName)
            } else {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", getApplication<Application>().packageName)
                putExtra("app_uid", getApplication<Application>().applicationInfo.uid)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        getApplication<Application>().startActivity(intent)
    }
}