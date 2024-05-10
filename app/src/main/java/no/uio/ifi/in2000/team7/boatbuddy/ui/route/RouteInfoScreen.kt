package no.uio.ifi.in2000.team7.boatbuddy.ui.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInfoScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    locationForecastViewModel: LocationForecastViewModel
) {

    val routeUIState by profileViewModel.routeScreenUIState.collectAsState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Rute info")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            profileViewModel.updateSelectedRoute(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            if (routeUIState.selectedRouteMap != null) {
                val route = routeUIState.selectedRouteMap!!.route
                Column {
                    // boat name
                    Text(text = route.boatname)
                    // route name
                    Text(text = route.routename)
                    // route description
                    Text(text = route.routeDescription)
                    // route time
                    Text(text = route.start + " - " + route.finish)
                    // route map
                    AsyncImage(
                        model = routeUIState.selectedRouteMap!!.mapURL,
                        contentDescription = "Map with path",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    // route actions
                    Row {
                        // route show on map
                        Button(
                            onClick = {
                                // TODO show it on the main map with a function or something
                                locationForecastViewModel.deselectWeekDayForecastRoute()
                                mainViewModel.displayRouteOnMap(route.route)
                                profileViewModel.updatePickedRoute(routeUIState.selectedRouteMap)
                                navController.navigate(Screen.HomeScreen.route)
                                mainViewModel.selectScreen(0) // select homescreen

                            }
                        ) {
                            Text(text = "Vis på hovedkart")
                        }
                        // route delete
                        // TODO add a button that deletes the route, make it red or something and make it go down the viewmodels i profileviewmodel
                    }
                }

            } else {
                CircularProgressIndicator()
            }
        }
    }
}