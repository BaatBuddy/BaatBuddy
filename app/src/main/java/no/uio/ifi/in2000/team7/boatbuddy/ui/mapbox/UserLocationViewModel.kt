package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

class UserLocationViewModel : ViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var locationState by mutableStateOf<LocationResult?>(null) // User Location
    var permissionsGranted by mutableStateOf(false)

    @SuppressLint("MissingPermission")
    private fun createLocationRequest(context: Context, permissionGranted: String) {

        // If fine location is granted
        if (permissionGranted == permission.ACCESS_FINE_LOCATION) {
            permissionsGranted = true
            locationRequest = LocationRequest.Builder(5000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(100)
                .build()
        }
        // If coarse location is granted
        else if (permissionGranted == permission.ACCESS_COARSE_LOCATION) {
            permissionsGranted = true
            locationRequest = LocationRequest.Builder(10000) //10000
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateIntervalMillis(500) // 5000
                .build()
        }
        // No permission granted
        else {

        }

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val settingsClient = LocationServices.getSettingsClient(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(locationSettingsRequest)
        task.addOnCompleteListener {
            if (task.isSuccessful) {
                getLocation()
            } else {
                // Handle exception?
                val e = task.exception
            }
        }

    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationState = locationResult
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun fetchLocation(context: Context, permissionGranted: String) {
        createLocationRequest(context, permissionGranted)
        createLocationCallback()
    }


    fun fetchUserLocation(context: Context): Location? {
        if (permissionsGranted) {
            return locationState?.lastLocation
        }
        return null
    }


}
