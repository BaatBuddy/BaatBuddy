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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherCalculatorRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import javax.inject.Inject


data class MapboxUIState(

    val mapView: MapView? = null,
    val cameraOptions: CameraOptions? = null,

    val polygonAlerts: MutableList<AlertPolygon> = mutableListOf(),
    val alertVisible: Boolean = false,

    val lastRouteData: APIStatus = APIStatus.Failed,
    val routeData: APIStatus = APIStatus.Failed,

    val routePoints: List<Point> = mutableListOf(),
    val routePath: List<Point>? = null,

    val generatedRoute: RouteMap? = null,
    val routeGenerated: Boolean = false,
    val hasGeneratedRoute: Boolean = false,
    val isDrawingRoute: Boolean = false,
)

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val mapboxRepository: MapboxRepository,
    private val weatherCalculatorRepository: WeatherCalculatorRepository
) : ViewModel() {

    private var _mapboxUIState: MutableStateFlow<MapboxUIState> = MutableStateFlow(MapboxUIState())
    var mapboxUIState: StateFlow<MapboxUIState> = _mapboxUIState

    private var initialized = false

    @MainThread
    fun initialize(context: Context, cameraOptions: CameraOptions) {
        if (initialized) return
        initialized = true
        createMap(context = context, cameraOptions = cameraOptions)
        observeMapInitialization()
        Log.d("InitializeCall", "initialize map")
    }

    private fun observeMapInitialization() {
        viewModelScope.launch {
            mapboxRepository.isMapInitialized.collect { isInitialized ->
                Log.d("LoadMap", "Map initialized: $isInitialized")
            }
        }
    }

    private fun createMap(context: Context, cameraOptions: CameraOptions) {
        //delay(5000L) // Lose internet access here


        val mapView =
            mapboxRepository.createMap(
                context = context,
                cameraOptions = cameraOptions
            )

        _mapboxUIState = MutableStateFlow(
            MapboxUIState(
                mapView = mapView,
                cameraOptions = cameraOptions
            )
        )
        mapboxUIState = _mapboxUIState

    }

    fun panToUser() {
        viewModelScope.launch(Dispatchers.IO) {
            mapboxRepository.panToUserOnMap()
        }
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

    fun resetRoutePath() {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    routePath = null
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

    // TODO fix
//    fun changeStyle(
//        style: String
//    ) {
//        viewModelScope.launch {
//            mapboxRepository.changeStyle(
//                style = style
//            )
//            _mapboxUIState.update {
//                it.copy(
//                    style = style
//                )
//            }
//        }
//    }

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
        }
    }

    fun toggleRouteClicking() {
        viewModelScope.launch {
            mapboxRepository.toggleRouteClicking()
        }
    }

    private fun updateRoute() {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    routePoints = mapboxRepository.getRoutePoints()
                )
            }
        }
    }

    fun generateRoute() {
        emptyGeneratedRoute()
        updateRoute()
        viewModelScope.launch {
            // init Loading
            _mapboxUIState.update {
                it.copy(
                    lastRouteData = APIStatus.Failed,
                    routeData = APIStatus.Loading
                )
            }
            // fetch data from autoroute api and handle the data
            when (val routeData = mapboxRepository.fetchRouteData()) {
                APIStatus.Failed -> {
                    _mapboxUIState.update {
                        it.copy(
                            lastRouteData = _mapboxUIState.value.routeData,
                            routeData = APIStatus.Failed
                        )
                    }
                }

                // do nothing if loading
                APIStatus.Loading -> {}

                // draw route on map
                is APIStatus.Success -> {
                    _mapboxUIState.update {
                        it.copy(
                            routeData = routeData,
                            generatedRoute = when (val data = routeData.data) {
                                is AutorouteData -> {
                                    val convertedPath = mapboxRepository.convertListToPoint(
                                        data.geometry.coordinates
                                    )

                                    mapboxRepository.createRoute(
                                        convertedPath
                                    )

                                    RouteMap(
                                        // dummy Route
                                        route = Route(
                                            username = "",
                                            boatname = "",
                                            routeID = -1,
                                            routename = "",
                                            routeDescription = "",
                                            route = convertedPath,
                                            start = "",
                                            finish = ""
                                        ),
                                        mapURL = mapboxRepository.generateMapURI(convertedPath)
                                    )


                                }

                                else -> null
                            }
                        )
                    }
                }
            }
        }
    }

    fun emptyGeneratedRoute() {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    generatedRoute = null
                )
            }
        }
    }

    fun refreshRoute() {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    routeGenerated = false
                )
            }
            mapboxRepository.refreshRoute()
        }
    }

    fun undoClick() {
        viewModelScope.launch {
            mapboxRepository.undoClick()
        }
    }

    fun redoClick() {
        viewModelScope.launch {
            mapboxRepository.redoClick()
        }
    }

    fun updateGeneratedRoute(state: Boolean) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    routeGenerated = state,
                    hasGeneratedRoute = true
                )
            }
        }
    }

    fun updateIsDrawingRoute(state: Boolean) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(
                    isDrawingRoute = state,
                )
            }
        }
    }
}