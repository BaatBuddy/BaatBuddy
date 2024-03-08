package no.uio.ifi.in2000.team7.boatbuddy.ui.oceanforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastData

data class OceanForecastUIState(
    val oceanForecastData: OceanForecastData? = null
)

class OceanForecastViewModel : ViewModel() {
    private val repository: OceanForecastRepository = OceanForecastRepository()

    private val _oceanForecastUIState = MutableStateFlow(OceanForecastUIState(null))
    val oceanForecastUIState: StateFlow<OceanForecastUIState> = _oceanForecastUIState.asStateFlow()

    private var initialized = false
    private var lastPosDate = ""

    @MainThread
    fun initialize(lat: String, lon: String) {
        // checks if the last initialize call has identical data
        initialized = lastPosDate == lat + lon

        if (initialized) return

        initialized = true
        lastPosDate = lat + lon

        loadOceanForecastData(lat, lon)
    }

    private fun loadOceanForecastData(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val oceanForecastData = repository.getOceanForecastData(lat, lon)
            _oceanForecastUIState.update {
                it.copy(
                    oceanForecastData = oceanForecastData
                )
            }
        }
    }
}