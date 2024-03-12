package no.uio.ifi.in2000.team7.boatbuddy.ui.sunrise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData


data class SunriseUIState(
    val sunriseData: SunriseData? = null
)
class SunriseViewModel: ViewModel() {
    private val repository: SunriseRepository = SunriseRepository()

    private val _sunriseUIState = MutableStateFlow(SunriseUIState(null))
    val sunriseUIState: StateFlow<SunriseUIState> = _sunriseUIState.asStateFlow()

    private var initialized = false
    private var lastPosDate = ""

    @MainThread
    fun initialize(lat: String, lon: String, date: String) {
        // checks if the last initialize call has identical data
        initialized = lastPosDate == lat+lon+date

        if (initialized) return

        initialized = true
        lastPosDate = lat+lon+date

        loadSunriseData(lat, lon, date)
    }

    private fun loadSunriseData(lat: String, lon: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val sunriseData = repository.getSunriseData(lat, lon, date)
            _sunriseUIState.update {
                it.copy(
                    sunriseData = sunriseData
                )
            }
        }
    }
}