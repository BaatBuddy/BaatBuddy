package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.content.Context
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
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.autoroute.AutorouteData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
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
) : ViewModel() {

    private var _mapboxUIState: MutableStateFlow<MapboxUIState> = MutableStateFlow(MapboxUIState())
    var mapboxUIState: StateFlow<MapboxUIState> = _mapboxUIState

    private var initialized = false

    @MainThread
    fun initialize(context: Context, cameraOptions: CameraOptions) {
        if (initialized) return
        initialized = true
        createMap(context = context, cameraOptions = cameraOptions)
        panToUser()
    }

    private fun createMap(context: Context, cameraOptions: CameraOptions) {

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

    private fun emptyGeneratedRoute() {
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