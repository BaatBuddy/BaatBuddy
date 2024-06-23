package no.uio.ifi.in2000.team7.boatbuddy.ui.main

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.model.internet.Status
import no.uio.ifi.in2000.team7.boatbuddy.data.internet.NetworkConnectivityObserver
import javax.inject.Inject

@HiltViewModel
class NetworkConnectivityViewModel @Inject constructor(private val connectivityObserver: NetworkConnectivityObserver) :
    ViewModel() {

    private var initialized = false
    private val _connectionUIState =
        MutableStateFlow(Status.NOSTATUS)
    val connectionUIState: StateFlow<Status> = _connectionUIState

    @MainThread
    fun initialize() {
        if (initialized) return
        initialized = true
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _connectionUIState.value = status
            }
        }
    }
}