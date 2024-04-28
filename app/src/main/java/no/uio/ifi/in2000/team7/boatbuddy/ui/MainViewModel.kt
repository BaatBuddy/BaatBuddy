package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MainScreenUIState(
    val splashScreenReady: Boolean = false,
    val showDialog: Boolean = false,

    )

class MainViewModel : ViewModel() {

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

    fun showDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDialog = true
                )
            }
        }
    }


    fun hideDialog() {
        viewModelScope.launch {
            _mainScreenUIState.update {
                it.copy(
                    showDialog = false
                )
            }
        }
    }
}