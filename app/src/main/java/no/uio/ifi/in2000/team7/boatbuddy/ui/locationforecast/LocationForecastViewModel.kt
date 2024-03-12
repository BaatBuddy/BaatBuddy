package no.uio.ifi.in2000.team7.boatbuddy.ui.locationforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepo
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.dto.LocationForcastCompactDTO

data class LocationForecastUiState(
    val locationForecast: LocationForcastCompactDTO?
)

class LocationForecastViewModel : ViewModel() {
    private val repository: LocationForecastRepository = LocationForecastRepository()

    private val _locationForecastUiState = MutableStateFlow(LocationForecastUiState(null))
    val locationForecastUiState: StateFlow<LocationForecastUiState> = _locationForecastUiState

    init {
        loadLocationForecast()

    }


    private fun loadLocationForecast() {
        viewModelScope.launch(Dispatchers.IO) {

            val locationForecast = repository.getLocationForecastData()
            _locationForecastUiState.update { it.copy(locationForecast = locationForecast) }
        }
    }
}