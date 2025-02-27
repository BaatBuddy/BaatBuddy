package no.uio.ifi.in2000.team7.boatbuddy.ui.main

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowFinishDialog
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowNoDialog
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowStartDialog
import javax.inject.Inject


data class MainScreenUIState(
    val splashScreenReady: Boolean = false,
    val showDialog: Dialog = ShowNoDialog,
    val selectedScreen: Int = 0,
    val isTrackingUser: Boolean = false,
    val showBottomBar: Boolean = true,

    val showLocationDialog: Boolean = false,
    val showNotificationDialog: Boolean = false,
    val showNoUserDialog: Boolean = false,

    val showDeleteRouteDialog: Boolean = false,
    val launched: Boolean = false,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapboxRepository: MapboxRepository,
    private val profileRepository: ProfileRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState = _mainScreenUIState.asStateFlow()


    init {

        viewModelScope.launch(Dispatchers.IO) {
            delay(1500L)
            _mainScreenUIState.update {
                it.copy(
                    splashScreenReady = true
                )
            }
        }

        updateIsTracking()

    }


    fun updateLaunched(state: Boolean) {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    launched = state
                )
            }
        }
    }

    // takes user to settings and depending on the API version which screen in the settings
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

    private fun updateIsTracking() {
        viewModelScope.launch(Dispatchers.IO) {
            val isTracking = profileRepository.getSelectedUser()?.isTracking ?: false
            _mainScreenUIState.update {
                it.copy(
                    isTrackingUser = isTracking
                )
            }
            /*if (isTracking && hasLocationPermission(mapboxRepository.context)) {
                mapboxRepository.startFollowUserOnMap()
            }*/
        }
    }


    fun showStartDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDialog = ShowStartDialog
                )
            }
        }
    }

    fun showFinishDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDialog = ShowFinishDialog
                )
            }
        }
    }


    fun hideDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDialog = ShowNoDialog
                )
            }
        }
    }

    fun selectScreen(screenIndex: Int) {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    selectedScreen = screenIndex
                )
            }
        }
    }

    fun startFollowUserOnMap() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainScreenUIState.update {
                it.copy(
                    isTrackingUser = true
                )
            }
            mapboxRepository.startFollowUserOnMap()
            profileRepository.startTrackingUser()

        }
    }

    fun stopFollowUserOnMap() {
        viewModelScope.launch(Dispatchers.IO) {
            mapboxRepository.stopFollowUserOnMap()
        }
    }

    fun stopTrackingUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainScreenUIState.update {
                it.copy(
                    isTrackingUser = false
                )
            }
            profileRepository.stopTrackingUser()
        }
    }

    fun showBottomBar() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showBottomBar = true
                )
            }
        }
    }

    fun hideBottomBar() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showBottomBar = false
                )
            }
        }
    }

    fun displayRouteOnMap(points: List<Point>) {
        viewModelScope.launch {
            mapboxRepository.createLinePath(points = points)
        }
    }


    fun hideLocationDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showLocationDialog = false
                )
            }
        }
    }

    fun showLocationDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainScreenUIState.update {
                it.copy(
                    showLocationDialog = true
                )
            }
        }
    }

    fun showNoUserDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainScreenUIState.update {
                it.copy(
                    showNoUserDialog = true
                )
            }
        }
    }

    fun hideNoUserDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainScreenUIState.update {
                it.copy(
                    showNoUserDialog = false
                )
            }
        }
    }

    fun showNotificationDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showNotificationDialog = true
                )
            }
        }
    }


    fun hideNotificationDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showNotificationDialog = false
                )
            }
        }
    }

    fun updateShowDeleteRouteDialog(state: Boolean) {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDeleteRouteDialog = state
                )
            }
        }
    }
}