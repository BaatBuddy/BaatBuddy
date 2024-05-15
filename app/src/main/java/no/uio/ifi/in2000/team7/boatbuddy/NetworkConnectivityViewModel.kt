package no.uio.ifi.in2000.team7.boatbuddy

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.ui.NetworkConnectivityObserver
import javax.inject.Inject

@HiltViewModel
class NetworkConnectivityViewModel @Inject constructor(private val connectivityObserver: NetworkConnectivityObserver) :
    ViewModel() {

    private var initialized = false
    private val _connectionUIState =
        MutableStateFlow(NetworkConnectivityObserver.Status.NoStatus)
    val connectionUIState: StateFlow<NetworkConnectivityObserver.Status> = _connectionUIState

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