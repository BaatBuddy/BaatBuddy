package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel

@Composable
fun FloatingMapButtons(
    homeViewModel: HomeViewModel,
    mapboxViewModel: MapboxViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    //networkConnectivityViewModel: NetworkConnectivityViewModel
) {

    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val homeScreenUIState by homeViewModel.homeScreenUIState.collectAsState()

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        // info button that explains how to use this screen
        FloatingActionButton(
            onClick = {
                homeViewModel.updateShowExplanationCard(true)
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary

        ) {
            Icon(
                painter = painterResource(id = R.drawable.instruction_icon),
                contentDescription = "info"
            )
        }

        // toggles the alert to be visible
        FloatingActionButton(
            onClick = {
                mapboxViewModel.toggleAlertVisibility()
            },
            shape = CircleShape,
            containerColor =
            if (mapboxUIState.alertVisible) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.primaryContainer,
            contentColor =
            if (mapboxUIState.alertVisible) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 16.dp)

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
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.center_user_icon),
                contentDescription = "sentrer til bruker posisjon"
            )
        }

        // all drawing a route
        Column {
            if (mapboxUIState.isDrawingRoute) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // undo button
                    ExtendedFloatingActionButton(
                        onClick = {
                            mapboxViewModel.undoClick()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.undo_icon
                            ),
                            contentDescription = "angre"
                        )
                    }

                    // refresh button
                    ExtendedFloatingActionButton(
                        onClick = {
                            mapboxViewModel.refreshRoute()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                    ) {
                        Icon(Icons.Filled.Refresh, "start på nytt")
                    }

                    // redo button
                    ExtendedFloatingActionButton(
                        onClick = {
                            mapboxViewModel.redoClick()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.redo_icon
                            ),
                            contentDescription = "gjør om"
                        )
                    }
                }
            }
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
                    contentColor = if (mapboxUIState.isDrawingRoute) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    modifier = if (mapboxUIState.isDrawingRoute) Modifier
                        .weight(1f)
                        .padding(end = 5.dp)
                    else Modifier
                ) {
                    if (!mapboxUIState.isDrawingRoute) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 4.dp)
                        )
                        Text(text = "Tegn rute")
                    } else {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.cancel_icon
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 4.dp)

                        )
                        Text(text = "Avbryt")
                    }
                }

                if (mapboxUIState.isDrawingRoute) {

                    // starts generating a route
                    ExtendedFloatingActionButton(
                        onClick = {
                            locationForecastViewModel.deselectWeekDayForecastRoute()
                            mapboxViewModel.updateGeneratedRoute(true)
                            mapboxViewModel.updateIsDrawingRoute(false)
                            mapboxViewModel.generateRoute()
                            mapboxViewModel.toggleRouteClicking()
                            homeViewModel.resetBottomSheet()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 4.dp)
                        )
                        Text(text = "Generer rute")
                    }
                }
            }
        }
    }
}