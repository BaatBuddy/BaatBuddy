package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.location_forecast.LocationForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.LocationForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.OceanForecastData
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData

data class WeatherUIState(
    val locationForecast: LocationForecastData? = null,
    val oceanForecast: OceanForecastData? = null,
    val sunrise: SunriseData? = null,
    val metAlerts: MetAlertsData? = null
)

class InfoScreenViewModel : ViewModel() {
    private val locationForecastRepository: LocationForecastRepository =
        LocationForecastRepository()
    private val oceanForecastRepository: OceanForecastRepository = OceanForecastRepository()
    private val sunriseRepository: SunriseRepository = SunriseRepository()
    private val metAlertsRepository: MetAlertsRepository = MetAlertsRepository()

    private val _weatherUIState = MutableStateFlow(WeatherUIState())
    val weatherUIState: MutableStateFlow<WeatherUIState> = _weatherUIState

    private var initialized = false
    private var lastPosAlt = ""

    @MainThread
    fun initialize(lat: String, lon: String, altitude: String) {
        initialized = lastPosAlt == lat + lon + altitude

        if (initialized) return

        initialized = true
        lastPosAlt = lat + lon + altitude
        loadWeatherData(lat, lon, altitude)

    }

    private fun loadWeatherData(
        lat: String,
        lon: String,
        altitude: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val locationForecast =
                locationForecastRepository.getLocationForecastData(lat, lon, altitude)
            val oceanForecast = oceanForecastRepository.getOceanForecastData(lat, lon)
            val sunrise = sunriseRepository.getSunriseData(lat, lon, "2024-03-15") // Fix later
            val metAlerts = metAlertsRepository.getMetAlertsData() // add list of lat and lon

            _weatherUIState.update { it.copy(locationForecast, oceanForecast, sunrise, metAlerts) }
        }
    }
}