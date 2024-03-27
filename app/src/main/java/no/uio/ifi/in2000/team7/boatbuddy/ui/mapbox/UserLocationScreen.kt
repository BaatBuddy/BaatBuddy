package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Start() {
    val viewModel: UserLocationViewModel = viewModel()
    val locationState = viewModel.locationState
    CheckPermissions(viewModel)
}

@Composable
fun CheckPermissions(viewModel: UserLocationViewModel) {

    val permissionsGranted = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionsList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    if (!permissionsGranted.value) {

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION, false
                ) -> {
                    // Precise location access granted
                    viewModel.fetchLocation(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    permissionsGranted.value = true
                }

                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION, false
                ) -> {
                    // Only approximate location access granted
                    viewModel.fetchLocation(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    permissionsGranted.value = true
                }

                else -> {
                    // No location access granted
                }
            }
        }
        /*Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                locationPermissionLauncher.launch(permissionsList)
            }) {
                Text("Share location")
            }
        }*/

    }

    //if (permissionsGranted.value){
    //RequestBackgroundLocation()
    //}

}

@Composable
fun RequestBackgroundLocation() {

    val context = LocalContext.current
    val activity = context as Activity

    // We ask for background location access
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    ) {
        /* Show educational UI explaining why we need background location access.
        Allow user to choose between continuing to use the app without granting permission,
        or navigate them to settings where they can grant permission. */
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Our app requests background location access to enhance your boating experience. " +
                        "While not essential, this permission allows us to offer seamless navigation and " +
                        "timely weather alerts, even when you're not actively using the app. " +
                        "Granting this access helps us provide a more convenient and enjoyable voyage without " +
                        "interrupting your sea adventures.",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = {
                    // Navigate to settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.parse("package:${context.packageName}")
                    intent.setData(uri)
                    context.startActivity(intent)
                }
            )
            { Text("Enable access") }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = {
                    // Go back
                }
            )
            { Text("Dismiss") }

        }

    }


}
