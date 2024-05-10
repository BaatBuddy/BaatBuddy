package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    metalertsViewModel: MetAlertsViewModel,
    mapboxViewModel: MapboxViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    snackbarHostState: SnackbarHostState
) {

    val context = LocalContext.current

    // fetches all alerts (no arguments)
    metalertsViewModel.initialize()


    mainViewModel.selectScreen(0)

    val metAlertsUIState by metalertsViewModel.metalertsUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val homeScreenUIState by homeViewModel.homeScreenUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()

    // bottom sheet setup
    val sheetState = rememberModalBottomSheetState()

    // foreground location setup
    val locationService = LocationService()
    metAlertsUIState.metalerts?.features?.let { locationService.initisializeAlerts(it) }

    var showAlert by remember { mutableStateOf(false) }
    var generateRoute by remember { mutableStateOf(false) }
    var createdRoute by remember { mutableStateOf(false) }

    if (mapboxUIState.routeData is APIStatus.Success && !homeScreenUIState.showBottomSheetInitialized) {
        homeViewModel.showBottomSheet()
    }

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
                        .padding(top = 80.dp, end = 48.dp)
                )
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (homeScreenUIState.showBottomSheetInitialized) {
                        ExtendedFloatingActionButton(
                            text = { Text("Show bottom sheet") },
                            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                            onClick = {
                                homeViewModel.showBottomSheet()
                            },
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    }
                    Row {
                        if (generateRoute) {
                            ExtendedFloatingActionButton(
                                text = { Text(text = "Generer rute") },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = ""
                                    )
                                },
                                // TODO move into UIstate and collect weather data on click
                                onClick = {
                                    generateRoute = false
                                    createdRoute = true
                                    mapboxViewModel.generateRoute()
                                    mapboxViewModel.toggleRouteClicking()
                                    homeViewModel.resetBottomSheet()
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
                                // TODO move into UIstate
                                generateRoute = !generateRoute
                                if (createdRoute || !generateRoute) {
                                    mapboxViewModel.refreshRoute()
                                    createdRoute = false
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

    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            AndroidView(
                factory = { _ ->
                    mapboxUIState.mapView
                }
            )


            if (homeScreenUIState.showBottomSheet) {

                ModalBottomSheet(
                    onDismissRequest = {
                        homeViewModel.hideBottomSheet()
                    },
                    sheetState = sheetState
                ) {
                    if (mapboxUIState.routePoints.isNotEmpty()) {
                        locationForecastViewModel.loadWeekdayForecastRoute(
                            mapboxUIState.routePoints
                        )
                        if (!metAlertsUIState.fetched && mapboxUIState.generatedRoute?.route?.route != null) {
                            metalertsViewModel.getAlerts(mapboxUIState.generatedRoute!!.route.route)
                        }
                    }
                    SwipeUpContent(
                        locationForecastUIState.weekdayForecastRoute,
                        profileViewModel = profileViewModel,
                        mainViewModel = mainViewModel,
                        mapboxViewModel = mapboxViewModel,
                        navController = navController,
                        infoScreenViewModel = infoScreenViewModel,
                        homeViewModel = homeViewModel,
                        locationForecastViewModel = locationForecastViewModel,
                        metalertsViewModel = metalertsViewModel,

                    )
                }
            }
            if (mapboxUIState.routeData is APIStatus.Loading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}