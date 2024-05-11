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
    val locationForecast: LocationForecastData? = null,

    val weekdayForecastUser: WeekForecast? = null,
    val selectedDayUser: DayForecast? = null,
    val fetchedUser: Boolean = false,

    val weekdayForecastRoute: WeekForecast? = null,
    val selectedDayRoute: DayForecast? = null,
    val fetchedRoute: Boolean = false,
)

@HiltViewModel
class LocationForecastViewModel @Inject constructor(
    private val locationForecastRepository: LocationForecastRepository,
    private val weatherCalculatorRepository: WeatherCalculatorRepository,
) : ViewModel() {

    private val _locationForecastUIState =
        MutableStateFlow(LocationForecastUIState())
    val locationForecastUIState: StateFlow<LocationForecastUIState> = _locationForecastUIState

    private var initialized = false
    private var lastPos = ""

    private var routeInit = false
    private var userInit = false

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
            refreshInitRoute()
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastRoute = null,
                    selectedDayRoute = null,
                )
            }
        }
    }

    fun loadWeekdayForecastRoute(points: List<Point>) {
        if (routeInit) return
        routeInit = true
        viewModelScope.launch(Dispatchers.IO) {
            val weekdayForecast = weatherCalculatorRepository.getWeekdayForecastData(points)
            if(weekdayForecast.days.isEmpty()){
                return@launch
            }
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastRoute = weekdayForecast,
                    selectedDayRoute = weekdayForecast.days.toList()[0].second,
                    fetchedRoute = true,
                )
            }
        }
    }

    fun loadWeekdayForecastUser(point: Point) {
        if (userInit) return
        userInit = true
        viewModelScope.launch(Dispatchers.IO) {
            val weekdayForecast =
                weatherCalculatorRepository.getWeekdayForecastData(listOf(point))
            if(weekdayForecast.days.isEmpty()){
                return@launch
            }
            _locationForecastUIState.update {
                it.copy(
                    weekdayForecastUser = weekdayForecast,
                    selectedDayUser = weekdayForecast.days.toList()[0].second,
                    fetchedUser = true,
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

    fun refreshInitUser() {
        viewModelScope.launch {
            userInit = false
        }
    }

    private fun refreshInitRoute() {
        viewModelScope.launch {
            routeInit = false
        }
    }

    fun updateScore() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_locationForecastUIState.value.fetchedUser) {
                _locationForecastUIState.update {
                    it.copy(
                        weekdayForecastUser = weatherCalculatorRepository.updateWeekForecastScore(
                            _locationForecastUIState.value.weekdayForecastUser!!
                        )
                    )
                }

            }
            if (_locationForecastUIState.value.fetchedRoute) {
                _locationForecastUIState.update {
                    it.copy(
                        weekdayForecastRoute = weatherCalculatorRepository.updateWeekForecastScore(
                            _locationForecastUIState.value.weekdayForecastRoute!!
                        )
                    )
                }
            }
        }
    }
}