package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
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
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapboxRepository: MapboxRepository,
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
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    isTrackingUser = true
                )
            }
            mapboxRepository.startFollowUserOnMap()
        }
    }

    fun stopFollowUserOnMap() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    isTrackingUser = false
                )
            }
            mapboxRepository.stopFollowUserOnMap()
        }
    }
}