package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import UserLocationViewModel
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationResult

@Composable
fun GetUserLocation() {

    val viewModel: UserLocationViewModel = viewModel()
    val context = LocalContext.current
    val permissionsList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION, false
            ) -> {
                // Precise location access granted
                viewModel.fetchLocation(context, Manifest.permission.ACCESS_FINE_LOCATION)
            }

            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION, false
            ) -> {
                // Only approximate location access granted
                viewModel.fetchLocation(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            else -> {
                // No location access granted
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!viewModel.permissionsGranted) {
            locationPermissionLauncher.launch(permissionsList)
        }
    }

    displayLocation(viewModel.locationState)

}

fun displayLocation(locationState: LocationResult?) {

    if (locationState != null) {
        Log.d(
            "MBScreen",
            "Lokasjon: ${locationState.lastLocation?.latitude}, ${locationState.lastLocation?.longitude}"
        )
    }

}



