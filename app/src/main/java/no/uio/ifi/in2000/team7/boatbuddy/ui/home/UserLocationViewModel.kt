package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val fusedLocationClient = userLocationRepository.getFusedLocationClient()


    @SuppressLint("MissingPermission")
    fun getFineLocation() {
        Log.i("ASDASD", "ASDASDASD")
        viewModelScope.launch(Dispatchers.IO) {
            if (userLocationRepository.checkPermissions()) {
                try {
                    Log.i("ASDASD", "ASDASDASD")
                    // Call await in an IO dispatcher
                    withContext(Dispatchers.IO) {
                        fusedLocationClient.lastLocation.continueWith {
                            Log.i("ASDASD", it.result.toString())
                            if (it.result != null) {
                                Log.i("ASDASD", it.result.toString())
                            } else {
                                Log.i("ASDASD", "Location is null.")
                            }
                        }
                    }


                } catch (e: Exception) {
                    // Handle exception
                    Log.e("ASDASD", "Failed to get last location.", e)
                }
            } else {
                Log.i("ASDASD", "Location permission is not granted.")
            }
        }
    }


    fun requestLocationPermission() {
        viewModelScope.launch {

        }
    }

}