package no.uio.ifi.in2000.team7.boatbuddy.data.location.userlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserLocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var locationState by mutableStateOf<LocationResult?>(null)


    fun fetchUserLocation(): Point? {
        val location = locationState?.lastLocation
        return if (location != null) {
            Point.fromLngLat(location.longitude, location.latitude)
        } else {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        fetchUserLocation()
    }

    private fun createLocationRequest() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            -> {
                locationRequest = LocationRequest.Builder(5000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMinUpdateIntervalMillis(1000)
                    .build()
            }
            // If coarse location is granted
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            -> {
                locationRequest = LocationRequest.Builder(5000)
                    .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setMinUpdateIntervalMillis(1000)
                    .build()
            }
            // No permission granted
            else -> {

                return
            }
        }

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val settingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(locationSettingsRequest)
        task.addOnCompleteListener {
            if (task.isSuccessful) {
                getLocation()
            } else {
                // Handle exception?
            }
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationState = locationResult
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
                if (!p0.isLocationAvailable) {
                    return
                }
            }
        }
    }

    fun fetchLocation() {
        createLocationRequest()
        createLocationCallback()
    }
}