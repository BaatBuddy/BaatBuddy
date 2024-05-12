package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
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
) {

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
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                // info button that explains how to use this screen
                FloatingActionButton(
                    onClick = {
                        homeViewModel.updateShowInfoPopup(true)
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
                        imageVector = Icons.Filled.Info,
                        contentDescription = "info"
                    )
                }

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
                    else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 8.dp)

                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "vis varsling"
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

                // all drawing a route
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = if (mapboxUIState.isDrawingRoute || (mapboxUIState.hasGeneratedRoute && mapboxUIState.generatedRoute != null)) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (homeScreenUIState.showBottomSheetInitialized && !mapboxUIState.isDrawingRoute) {

                        // show bottom sheet
                        ExtendedFloatingActionButton(
                            text = { Text("Vis været") },
                            icon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "vis været"
                                )
                            },
                            onClick = {
                                homeViewModel.showBottomSheet()
                            }
                        )
                    }
                    if (mapboxUIState.isDrawingRoute) {

                        // starts generating a route
                        ExtendedFloatingActionButton(
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

                        // undo button
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

                        // refresh button
                        SmallFloatingActionButton(
                            onClick = {
                                mapboxViewModel.refreshRoute()
                            }
                        ) {
                            Icon(Icons.Filled.Refresh, "start på nytt")
                        }

                        // redo button
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

                    // start drawing or cancel drawing
                    ExtendedFloatingActionButton(
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

    if (homeScreenUIState.showInfoPopup) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {

            val modifier = Modifier
                .padding(horizontal = 16.dp)

            Dialog(
                onDismissRequest = {
                    homeViewModel.updateShowInfoPopup(false)
                }
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rute tegning",
                            modifier = modifier
                                .padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                        IconButton(
                            onClick = {
                                homeViewModel.updateShowInfoPopup(false)
                            },
                            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = modifier
                                .size(32.dp),
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.route_explenation),
                        contentDescription = "bilde av oslo med skissert rute",
                        modifier = modifier
                            .clip(RoundedCornerShape(8.dp)),
                    )

                    // heading text


                    // info text
                    Text(
                        text = "Her kan man trykke på tegn rute knappen helt nederst i høyre hjørne" +
                                " for å starte å tegne en rute på kartet. Deretter trykker man inn minst" +
                                " 2 og opp til 10 punkter for å lage en skisse av ruten. \n\nNå kan man trykke" +
                                " på generer rute knappen for å starte genereringen av en mer raffinert rute." +
                                " \n\nVæret for ruten vil automatisk dukke opp på bunnen, men frykt ikke for å" +
                                " fjerne den fordi man kan få informasjonen opp igjen med en 'vis vær' knapp.",
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 16.dp, top = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
            }
        }
    }
}

