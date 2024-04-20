package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import UserLocationViewModel
import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.background_location_tracking.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox.MapboxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    metAlertsViewModel: MetAlertsViewModel = viewModel(),
    mapboxViewModel: MapboxViewModel = viewModel(),
    userLocationViewModel: UserLocationViewModel = viewModel(),
) {

    val context = LocalContext.current

    // fetches all alerts (no arguments)
    metAlertsViewModel.initialize()
    mapboxViewModel.initialize(
        context = context,
        cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(10.20449, 59.74389))
            .zoom(10.0)
            .bearing(0.0)
            .pitch(0.0)
            .build(),
        style = "mapbox://styles/mafredri/clu8bbhvh019501p71sewd7eg"
    )

    val metAlertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()


//    val snackBarHostState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }




    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show bottom sheet") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }

    ) {
        AndroidView(
            factory = { ctx ->
                mapboxUIState.mapView
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                // Hide bottom sheet
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
                // Start and stop tracking
                Column {

                    Row {
                        Button(onClick = {
                            Intent(context, LocationService::class.java).apply {
                                action = LocationService.ACTION_START
                                context.startService(this)
                            }

                        }
                        ) {
                            Text(text = "Start")
                        }

                        Button(onClick = {
                            Intent(context, LocationService::class.java).apply {
                                action = LocationService.ACTION_STOP
                                context.startService(this)
                            }

                        }
                        ) {
                            Text(text = "Stop")
                        }
                    }
                }

            }
        }
    }

}