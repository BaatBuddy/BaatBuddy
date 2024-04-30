package no.uio.ifi.in2000.team7.boatbuddy

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
import no.uio.ifi.in2000.team7.boatbuddy.ui.BottomBar
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.OceanForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.SunriseViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateBoatScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateUserScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route.RouteScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.tracking.StartTrackingDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.tracking.StopTrackingDialog


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
    userLocationViewModel: UserLocationViewModel,
    homeViewModel: HomeViewModel
) {

    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                mainViewModel = mainViewModel,
                profileViewModel = profileViewModel,
            )
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
                        userLocationViewModel = userLocationViewModel,
                        locationForecastViewModel = locationForecastViewModel,
                        homeViewModel = homeViewModel,
                    )
                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen(
                        metAlertsViewModel = metalertsViewModel,
                        locationForecastViewModel = locationForecastViewModel,
                        oceanForecastViewModel = oceanforecastViewModel,
                        sunriseViewModel = sunriseViewModel
                    )
                }
                composable(route = Screen.SettingScreen.route) {
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        navController = navController
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
                        profileViewModel = profileViewModel
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




