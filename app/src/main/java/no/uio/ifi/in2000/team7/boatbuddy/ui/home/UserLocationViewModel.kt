package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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