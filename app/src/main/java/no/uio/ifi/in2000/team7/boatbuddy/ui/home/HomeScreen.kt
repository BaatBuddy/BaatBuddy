package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import no.uio.ifi.in2000.team7.boatbuddy.R
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
    val locationForecastUIState by locationForecastViewModel.locationForecastUIState.collectAsState()

    // bottom sheet setup
    val sheetState = rememberModalBottomSheetState()

    // foreground location setup
    val locationService = LocationService()
    metAlertsUIState.metalerts?.features?.let { locationService.initisializeAlerts(it) }

    if (mapboxUIState.routeData is APIStatus.Success && !homeScreenUIState.showBottomSheetInitialized) {
        homeViewModel.showBottomSheet()
    }

    Scaffold(
        // TODO center at user button
        // TODO center align buttons and adjust sizes
        // TODO adjust buttons so they are compatible with all phone sizes
        // TODO
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                // toggles the alert to be visible
                FloatingActionButton(
                    onClick = {
                        mapboxViewModel.toggleAlertVisibility();
                    },
                    shape = CircleShape,
                    containerColor =
                    if (mapboxUIState.alertVisible) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor =
                    if (mapboxUIState.alertVisible) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.primary

                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = ""
                    )
                }
                // centers the camera to user
                FloatingActionButton(
                    onClick = {
                        mapboxViewModel.panToUser()
                    },
                    contentColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.center_user_icon),
                        contentDescription = "sentrer til bruker posisjon"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = if (mapboxUIState.isDrawingRoute || (mapboxUIState.hasGeneratedRoute && mapboxUIState.generatedRoute != null)) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (homeScreenUIState.showBottomSheetInitialized && !mapboxUIState.isDrawingRoute) {
                        ExtendedFloatingActionButton(
                            text = { Text("Vis været") },
                            icon = { Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "") },
                            onClick = {
                                homeViewModel.showBottomSheet()
                            }
                        )
                    }
                    if (mapboxUIState.isDrawingRoute) {
                        FloatingActionButton(
                            onClick = {
                                mapboxViewModel.updateGeneratedRoute(true)
                                mapboxViewModel.updateIsDrawingRoute(false)
                                mapboxViewModel.generateRoute()
                                mapboxViewModel.toggleRouteClicking()
                                homeViewModel.resetBottomSheet()
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.route_icon
                                ),
                                contentDescription = ""
                            )
                        }

                        //Back-knapp
                        SmallFloatingActionButton(
                            onClick = {
                                mapboxViewModel.undoClick()
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.undo_icon
                                ),
                                contentDescription = "angre"
                            )
                        }
                        // Refresh-knapp
                        SmallFloatingActionButton(
                            onClick = {
                                mapboxViewModel.refreshRoute()
                            }
                        ) {
                            Icon(Icons.Filled.Refresh, "start på nytt")
                        }
                        //Forward-knapp
                        SmallFloatingActionButton(
                            onClick = {
                                mapboxViewModel.redoClick()
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.redo_icon
                                ),
                                contentDescription = "gjør om"
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            mapboxViewModel.updateIsDrawingRoute(!mapboxUIState.isDrawingRoute)
                            if (mapboxUIState.routeGenerated || !mapboxUIState.isDrawingRoute) {
                                mapboxViewModel.refreshRoute()
                            }
                            mapboxViewModel.toggleRouteClicking()
                        },
                        containerColor = if (mapboxUIState.isDrawingRoute) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (mapboxUIState.isDrawingRoute) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    ) {
                        if (!mapboxUIState.isDrawingRoute) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = ""
                            )
                        } else {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.cancel_icon
                                ),
                                contentDescription = ""
                            )
                        }
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
                    mapboxUIState.mapView // Her lages kartet
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

