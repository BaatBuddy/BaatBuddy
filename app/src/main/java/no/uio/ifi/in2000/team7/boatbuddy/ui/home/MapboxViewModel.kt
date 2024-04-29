package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherCalculatorRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import javax.inject.Inject


data class MapboxUIState(
    val mapView: MapView,
    val cameraOptions: CameraOptions,
    val style: String,

    val polygonAlerts: MutableList<AlertPolygon> = mutableListOf(),
    val alertVisible: Boolean = false,

    val routePoints: List<Point> = mutableListOf(),
    val routePath: List<Point>? = null,
)

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val mapboxRepository: MapboxRepository,
    private val weatherCalculatorRepository: WeatherCalculatorRepository
) : ViewModel() {


    private lateinit var _mapboxUIState: MutableStateFlow<MapboxUIState>
    lateinit var mapboxUIState: StateFlow<MapboxUIState>

    private var initialized = false

    @MainThread
    fun initialize(context: Context, cameraOptions: CameraOptions, style: String) {
        if (initialized) return
        initialized = true
        createMap(context = context, cameraOptions = cameraOptions, style = style)

    }


    private fun createMap(context: Context, cameraOptions: CameraOptions, style: String) {

        // mapview setup
        val mapView =
            mapboxRepository.createMap(
                context = context,
                cameraOptions = cameraOptions,
                style = style
            )

        _mapboxUIState = MutableStateFlow(
            MapboxUIState(
                mapView = mapView,
                cameraOptions = cameraOptions,
                style = style
            )
        )
        mapboxUIState = _mapboxUIState

    }

    fun toggleAlertVisibility() {
        viewModelScope.launch {
            mapboxRepository.toggleAlertVisibility()
            _mapboxUIState.update {
                it.copy(
                    alertVisible = !it.alertVisible
                )
            }
        }
    }

    private fun updateCameraOptions(cameraOptions: CameraOptions) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    cameraOptions = cameraOptions
                )
            }
        }
    }

    fun panToPoint(
        cameraOptions: CameraOptions
    ) {
        updateCameraOptions(cameraOptions = cameraOptions)
        viewModelScope.launch {
            mapboxRepository.panToPoint(cameraOptions = cameraOptions)
        }
    }

    fun changeStyle(
        style: String
    ) {
        viewModelScope.launch {
            mapboxRepository.changeStyle(
                style = style
            )
            _mapboxUIState.update {
                it.copy(
                    style = style
                )
            }
        }
    }

    fun createLinePath(
        points: List<Point>
    ) {
        viewModelScope.launch {
            mapboxRepository.createLinePath(points = points)
        }
    }

    fun calculateWeather(points: List<Point>) {
        viewModelScope.launch {
            val weatherPreferences = WeatherPreferences(
                waveHeight = 0.5,
                windSpeed = 4.0,
                airTemperature = 20.0,
                cloudAreaFraction = 20.0,
                waterTemperature = 20.0,
                relativeHumidity = 30.0,
                precipitationAmount = 0.0,
                fogAreaFraction = 0.0
            )

            val pathWeatherData =
                weatherCalculatorRepository.fetchPathWeatherData(points)
            Log.i(
                "ASDASD", WeatherScore.calculatePath(
                    pathWeatherData = pathWeatherData,
                    weatherPreferences = weatherPreferences
                ).toString()
            )
        }
    }

    fun toggleRouteClicking() {
        viewModelScope.launch {
            mapboxRepository.toggleRouteClicking()
        }
    }

    fun updateRoute() {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    routePoints = mapboxRepository.getRoutePoints()
                )
            }
        }
    }

    fun generateRoute() {
        updateRoute()
        viewModelScope.launch {
            val route = mapboxRepository.generateRoute()
            _mapboxUIState.update {
                it.copy(
                    routePath = route
                )
            }
        }
    }
}