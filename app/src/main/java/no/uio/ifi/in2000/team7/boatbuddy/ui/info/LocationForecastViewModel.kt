package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.util.Log
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
    val locationForecast: LocationForecastData? = null,

    val selectedDayRoute: DayForecast? = null,
    val weekdayForecastUser: WeekForecast? = null,
    val weekdayForecastRoute: WeekForecast? = null,

    val selectedDayUser: DayForecast? = null,
    val fetchedWeekDayUser: Boolean = false,
    val fetchedWeekDayRoute: Boolean = false,
)

@HiltViewModel
class LocationForecastViewModel @Inject constructor(
    private val locationForecastRepository: LocationForecastRepository,
    private val weatherCalculatorRepository: WeatherCalculatorRepository,
) : ViewModel() {

    private val _locationForecastUIState =
        MutableStateFlow(LocationForecastUIState())
    val locationForecastUiState: StateFlow<LocationForecastUIState> = _locationForecastUIState

    private var initialized = false
    private var lastPos = ""

    @MainThread
    fun initialize(point: Point, altitude: String = "") {
        initialized = lastPos == point.toJson()

        if (initialized) return

        initialized = true
        lastPos = point.toJson()
        loadLocationForecast(point, altitude)
    }

    // keeping altitude as optional if the function is not going to be private
    private fun loadLocationForecast(
        point: Point,
        altitude: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val locationForecast =
                locationForecastRepository.getLocationForecastData(point, altitude)

            _locationForecastUIState.update {
                it.copy(
                    locationForecast = locationForecast,
                )
            }

        }
    }

    fun deselectWeekDayForecastRoute() {
        viewModelScope.launch(Dispatchers.IO) {
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastRoute = null,
                    selectedDayRoute = null,
                    fetchedWeekDayRoute = false,
                )
            }
        }
    }

    fun loadWeekdayForecastRoute(points: List<Point>) {
        viewModelScope.launch(Dispatchers.IO) {
            val weekdayForecast = weatherCalculatorRepository.getWeekdayForecastData(points)
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastRoute = weekdayForecast,
                    selectedDayRoute = weekdayForecast.days.toList()[0].second,
                    fetchedWeekDayRoute = true,
                )
            }
        }

    }

    fun loadWeekdayForecastUser(point: Point) {
        viewModelScope.launch {
            val weekdayForecast = weatherCalculatorRepository.getWeekdayForecastData(listOf(point))
            Log.i("ASDASD", point.toString())
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastUser = weekdayForecast,
                    selectedDayUser = weekdayForecast.days.toList()[0].second,
                    fetchedWeekDayUser = true,
                )
            }
        }
    }

    fun updateSelectedDayUser(
        dayForecast: DayForecast
    ) {
        viewModelScope.launch {
            _locationForecastUIState.update {
                it.copy(
                    selectedDayUser = dayForecast
                )
            }
        }
    }

    fun updateSelectedDayRoute(
        dayForecast: DayForecast
    ) {
        viewModelScope.launch {
            _locationForecastUIState.update {
                it.copy(
                    selectedDayRoute = dayForecast
                )
            }
        }
    }
}