package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    metalertsViewModel: MetAlertsViewModel,
    mapboxViewModel: MapboxViewModel,
    userLocationViewModel: UserLocationViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    homeViewModel: HomeViewModel,
) {

    val context = LocalContext.current

    // fetches all alerts (no arguments)
    metalertsViewModel.initialize()
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

    val metAlertsUIState by metalertsViewModel.metalertsUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val homeScreenUIState by homeViewModel.homeScreenUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()

    // bottom sheet setup
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheetButton by remember { mutableStateOf(true) }


    // notification setup
    val settingsActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the result if needed
        }
    }

    // Show the dialog if required
    if (homeScreenUIState.showNotificationDialog) {
        NotificationOptInDialog(
            navigateToSettings = {
                homeViewModel.navigateToNotificationSettings()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    settingsActivityResultLauncher.launch(
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                    )
                } else {
                    settingsActivityResultLauncher.launch(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                }
            },
            onDismiss = { homeViewModel.hideNotificationDialog() }
        )
    }

    // foreground location setup
    val locationService = LocationService()
    metAlertsUIState.metalerts?.features?.let { locationService.initisializeAlerts(it) }

    var showAlert by remember { mutableStateOf(false) }
    var generateRoute by remember { mutableStateOf(false) }
    var createdRoute by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ElevatedFilterChip(
                    selected = showAlert,
                    onClick = { mapboxViewModel.toggleAlertVisibility(); showAlert = !showAlert },
                    label = {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = ""
                        )
                        Text(text = if (!showAlert) "Vis varsler" else "Skjul varsler")
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (showBottomSheetButton) {
                        ExtendedFloatingActionButton(
                            text = { Text("Show bottom sheet") },
                            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                            onClick = {
                                showBottomSheet = true
                                showBottomSheetButton = !showBottomSheetButton
                            },
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    }
                    Row {
                        if (generateRoute) {
                            showBottomSheetButton = false
                            ExtendedFloatingActionButton(
                                text = { Text(text = "Generer rute") },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = ""
                                    )
                                },
                                onClick = {
                                    generateRoute = false
                                    createdRoute = true
                                    mapboxViewModel.generateRoute()
                                    mapboxViewModel.toggleRouteClicking()
                                })

                            //Back-knapp
                            SmallFloatingActionButton(
                                onClick = {
                                    mapboxViewModel.undoClick()
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                            }
                            // Refresh-knapp
                            SmallFloatingActionButton(
                                onClick = {
                                    mapboxViewModel.refreshRoute()
                                }
                            ) {
                                Icon(Icons.Filled.Refresh, "")
                            }
                            //Forward-knapp
                            SmallFloatingActionButton(
                                onClick = {
                                    mapboxViewModel.redoClick()
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
                            }
                        }
                        ExtendedFloatingActionButton(
                            text = { Text(text = if (!generateRoute) "Tegn rute" else "Avbryt") },
                            icon = {
                                if (!generateRoute) Icon(
                                    imageVector = Icons.Filled.Create,
                                    contentDescription = ""
                                ) else Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = ""
                                )
                            },
                            onClick = {
                                generateRoute = !generateRoute
                                if (createdRoute || !generateRoute) {
                                    mapboxViewModel.refreshRoute()
                                }
                                mapboxViewModel.toggleRouteClicking()
                            },
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    }

                }
            }
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
                    showBottomSheetButton = true
                },
                sheetState = sheetState
            ) {
                if (mapboxUIState.routePoints.isNotEmpty()) {
                    locationForecastViewModel.loadWeekdayForecast(mapboxUIState.routePoints)
                    showBottomSheet = true
                }

                SwipeUpContent(locationForecastUIState)

                Column {
                    Row {

                        Button(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Text("Hide bottom sheet")
                        }
                        Button(onClick = { mapboxViewModel.toggleAlertVisibility() }) {
                            Text(text = "Toggle alert visibility")
                        }
                    }

                    Row {
                        Button(onClick = {


                        }
                        ) {
                            Text(text = "Start")
                        }

                        Button(onClick = {
                            Intent(context, locationService::class.java).apply {
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