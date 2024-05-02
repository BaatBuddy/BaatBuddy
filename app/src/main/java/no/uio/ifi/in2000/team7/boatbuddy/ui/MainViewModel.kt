package no.uio.ifi.in2000.team7.boatbuddy.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
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
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapboxRepository: MapboxRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState = _mainScreenUIState.asStateFlow()

    init {

        viewModelScope.launch {
            delay(1500L)
            _mainScreenUIState.update {
                it.copy(
                    splashScreenReady = true
                )
            }
        }

        updateIsTracking()

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

    // TODO check if this is allowed
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
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
}