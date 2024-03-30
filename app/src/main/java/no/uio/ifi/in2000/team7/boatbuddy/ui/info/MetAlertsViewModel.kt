package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import no.uio.ifi.in2000.team7.boatbuddy.ui.test.metalerts.MetAlertsUIState

data class MetAlertsUIState(
    val metalerts: MetAlertsData?
)

class MetAlertsViewModel : ViewModel() {

    private val repository: MetAlertsRepository = MetAlertsRepository()

    private val _metalertsUIState = MutableStateFlow(MetAlertsUIState(null))
    val metalertsUIState: StateFlow<MetAlertsUIState> = _metalertsUIState.asStateFlow()

    private var initialized = false
    private var lastPos = "initialize filler :)"

    @MainThread
    fun initialize(lat: String = "", lon: String = "") {
        initialized = lastPos == lat + lon

        if (initialized) return
        initialized = true
        lastPos = lat + lon
        loadMetalerts(lat, lon)

    }

    // keep default value in case function is going to be used globally
    private fun loadMetalerts(lat: String = "", lon: String = "") {

        viewModelScope.launch(Dispatchers.IO) {
            
            val metalerts = repository.getMetAlertsData(lat, lon)
            _metalertsUIState.update { it.copy(metalerts = metalerts) }

        }

    }
}