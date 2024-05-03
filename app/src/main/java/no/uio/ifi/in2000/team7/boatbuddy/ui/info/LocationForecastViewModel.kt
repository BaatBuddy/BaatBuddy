package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherCalculatorRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import javax.inject.Inject

data class LocationForecastUIState(
    val locationForecast: LocationForecastData?,
    val weekdayForecast: WeekForecast?,
    val selectedDay: DayForecast?
)

@HiltViewModel
class LocationForecastViewModel @Inject constructor(
    private val locationForecastRepository: LocationForecastRepository,
    private val weatherCalculatorRepository: WeatherCalculatorRepository,
) : ViewModel() {

    private val _locationForecastUIState =
        MutableStateFlow(LocationForecastUIState(null, null, null))
    val locationForecastUiState: StateFlow<LocationForecastUIState> = _locationForecastUIState

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

            val locationForecast =
                locationForecastRepository.getLocationForecastData(lat, lon, altitude)

            _locationForecastUIState.update {
                it.copy(
                    locationForecast = locationForecast,
                )
            }

        }
    }

    fun loadWeekdayForecast(points: List<Point>) {
        viewModelScope.launch {
            _locationForecastUIState.update {
                val weekdayForecast = weatherCalculatorRepository.getWeekdayForecastData(points)
                it.copy(
                    weekdayForecast = weekdayForecast,
                    selectedDay = weekdayForecast.days.toList()
                        .minBy { dayForecast -> dayForecast.second.date }.second
                )
            }
        }

    }

    fun updateSelectedDay(
        dayForecast: DayForecast
    ) {
        viewModelScope.launch {
            _locationForecastUIState.update {
                it.copy(
                    selectedDay = dayForecast
                )
            }
        }
    }
}