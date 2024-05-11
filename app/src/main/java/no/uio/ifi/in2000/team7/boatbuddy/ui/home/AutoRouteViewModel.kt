package no.uio.ifi.in2000.team7.boatbuddy.ui.home

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
import no.uio.ifi.in2000.team7.boatbuddy.data.autoroute.AutorouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import javax.inject.Inject

data class AutorouteUiState(
    val autoRoute: APIStatus = APIStatus.Failed
)

@HiltViewModel
class AutoRouteViewModel @Inject constructor(
    private val repository: AutorouteRepository
) : ViewModel() {

    private val _autoRouteUiState = MutableStateFlow(AutorouteUiState())
    val autoRouteUiState: StateFlow<AutorouteUiState> = _autoRouteUiState

    private var initialized = false
    private var lastCourse = listOf<Point>()

    @MainThread
    fun initialize(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String
    ) {
        initialized = lastCourse == course

        if (initialized) return

        initialized = true
        lastCourse = course
        loadAutoRoute(course, safetyDepth, safetyHeight, boatSpeed)

    }

    private fun loadAutoRoute(
        course: List<Point>,
        safetyDepth: String,
        safetyHeight: String,
        boatSpeed: String

    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val autoRoute =
                repository.getAutorouteData(course, safetyDepth, safetyHeight, boatSpeed)
            _autoRouteUiState.update { it.copy(autoRoute = autoRoute) }
        }

    }


}