package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        permissionResults.entries.forEach {
            when (it.key) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    if (it.value) {
                        // Precise location access granted
                        viewModel.fetchLocation(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        // Permission denied. Possibly you can explain to the user why you need the permission.
                    }
                }

                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    if (it.value) {
                        // Only approximate location access granted
                        viewModel.fetchLocation(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    } else {
                        // Permission denied. Possibly you can explain to the user why you need the permission.
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        if (context.checkPermission(permissionsList)) {
            viewModel.fetchLocation(context, Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationPermissionLauncher.launch(permissionsList)
        }
    }

    displayLocation(viewModel.locationState)

}

fun Context.checkPermission(permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun displayLocation(locationState: LocationResult?) {

    if (locationState != null) {
        Log.d(
            "MBScreen",
            "Lokasjon: ${locationState.lastLocation?.latitude}, ${locationState.lastLocation?.longitude}"
        )
    }

}



