package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowFinishDialog
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowStartDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.OceanForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.SunriseViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateBoatScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateUserScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.AddRouteScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.RouteInfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.RouteScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.StartTrackingDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.StopTrackingDialog


@Composable
fun NavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    mapboxViewModel: MapboxViewModel,
    metalertsViewModel: MetAlertsViewModel,
    oceanforecastViewModel: OceanForecastViewModel,
    profileViewModel: ProfileViewModel,
    sunriseViewModel: SunriseViewModel,
    homeViewModel: HomeViewModel
) {

    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()

    Scaffold(
        bottomBar = {
            if (mainScreenUIState.showBottomBar) {
                BottomBar(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    profileViewModel = profileViewModel,
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            ) {
                composable(route = Screen.HomeScreen.route) {
                    HomeScreen(
                        metalertsViewModel = metalertsViewModel,
                        mapboxViewModel = mapboxViewModel,
                        locationForecastViewModel = locationForecastViewModel,
                        homeViewModel = homeViewModel,
                        mainViewModel = mainViewModel,
                        navController = navController,
                    )
                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen(
                        metAlertsViewModel = metalertsViewModel,
                        locationForecastViewModel = locationForecastViewModel,
                        oceanForecastViewModel = oceanforecastViewModel,
                        sunriseViewModel = sunriseViewModel,
                        mainViewModel = mainViewModel,
                    )
                }
                composable(route = Screen.SettingsScreen.route) {
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        navController = navController,
                        mainViewModel = mainViewModel,
                    )
                }
                composable(route = "createuser") {
                    CreateUserScreen(
                        profileViewModel = profileViewModel,
                        navController = navController
                    )
                }
                composable(route = "createboat") {
                    CreateBoatScreen(
                        profileViewModel = profileViewModel,
                        navController = navController
                    )
                }
                composable(route = Screen.RouteScreen.route) {
                    RouteScreen(
                        profileViewModel = profileViewModel,
                        navController = navController,
                        mainViewModel = mainViewModel,
                    )
                }
                composable(route = "addroute") {
                    AddRouteScreen(
                        profileViewModel = profileViewModel,
                        navController = navController,
                        mainViewModel = mainViewModel,
                    )
                }
                composable(route = "routeinfo") {
                    RouteInfoScreen(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        profileViewModel = profileViewModel,

                        )
                }
            }
            if (mainScreenUIState.showDialog == ShowStartDialog) {
                StartTrackingDialog(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            } else if (mainScreenUIState.showDialog == ShowFinishDialog) {
                StopTrackingDialog(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    profileViewModel = profileViewModel,
                )
            }
        }

    }
}




