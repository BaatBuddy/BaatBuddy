package no.uio.ifi.in2000.team7.boatbuddy.ui.locationforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData

data class LocationForecastUiState(
    val locationForecast: LocationForecastData?
)

class LocationForecastViewModel : ViewModel() {
    private val repository: LocationForecastRepository = LocationForecastRepository()

    private val _locationForecastUiState = MutableStateFlow(LocationForecastUiState(null))
    val locationForecastUiState: StateFlow<LocationForecastUiState> = _locationForecastUiState

    private var initialized = false
    private var lastPosAlt = ""

    @MainThread
    fun initialize(lat: String, lon: String, altitude: String) {
        initialized = lastPosAlt == lat + lon + altitude

        if (initialized) return

        initialized = true
        lastPosAlt = lat + lon + altitude
        loadLocationForecast(lat, lon, altitude)


    }


    private fun loadLocationForecast(
        lat: String,
        lon: String,
        altitude: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val locationForecast = repository.getLocationForecastData(lat, lon, altitude)
            _locationForecastUiState.update { it.copy(locationForecast = locationForecast) }
        }
    }
}