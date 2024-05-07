package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.location.userlocation.UserLocationRepository
import javax.inject.Inject


data class UserLocationUIState(
    val userLocation: Point? = null,
)

@HiltViewModel
class UserLocationViewModel @Inject constructor(
    private val userLocationRepository: UserLocationRepository
) : ViewModel() {


    private val _userLocationUIState = MutableStateFlow(UserLocationUIState())
    val userLocationUIState: StateFlow<UserLocationUIState> = _userLocationUIState.asStateFlow()

    init {
        requestLocationPermission()
    }


    fun requestLocationPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            userLocationRepository.fetchLocation()
        }
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            _userLocationUIState.update {
                it.copy(
                    userLocation = userLocationRepository.fetchUserLocation()
                )
            }
        }
    }

}