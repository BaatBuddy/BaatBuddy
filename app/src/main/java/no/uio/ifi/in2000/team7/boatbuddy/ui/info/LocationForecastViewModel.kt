package no.uio.ifi.in2000.team7.boatbuddy.ui.info

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
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast

data class LocationForecastUiState(
    val locationForecast: LocationForecastData?,
    val weekdayForecast: WeekForecast?
)

class LocationForecastViewModel : ViewModel() {
    private val repository: LocationForecastRepository = LocationForecastRepository()

    private val _locationForecastUiState = MutableStateFlow(LocationForecastUiState(null, null))
    val locationForecastUiState: StateFlow<LocationForecastUiState> = _locationForecastUiState

    private var initialized = false
    private var lastPos = ""

    @MainThread
    fun initialize(lat: String, lon: String, altitude: String = "") {
        initialized = lastPos == lat + lon + altitude

        if (initialized) return

        initialized = true
        lastPos = lat + lon + altitude
        loadLocationForecast(lat, lon, altitude)
    }

    // keeping altitude as optional if the function is not going to be private
    private fun loadLocationForecast(
        lat: String,
        lon: String,
        altitude: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val locationForecast = repository.getLocationForecastData(lat, lon, altitude)
            val weekdayForecast = repository.getWeekdayForecastData(lat, lon, altitude)
            _locationForecastUiState.update {
                it.copy(
                    locationForecast = locationForecast,
                    weekdayForecast = weekdayForecast
                )
            }

        }
    }
}