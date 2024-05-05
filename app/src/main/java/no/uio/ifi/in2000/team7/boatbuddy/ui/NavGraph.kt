package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
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

    val context = LocalContext.current

    mapboxViewModel.initialize(
        context = context,
        cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(10.20449, 59.74389))
            .zoom(10.0)
            .bearing(0.0)
            .pitch(0.0)
            .build()
    )

    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // if route is either too long or points is not close enough to the water
    LaunchedEffect(mapboxUIState.lastRouteData) {
        if (mapboxUIState.routeData is APIStatus.Failed
            && mapboxUIState.lastRouteData is APIStatus.Loading
        ) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Ruten er for lang eller inneholder punkter på land",
                    duration = SnackbarDuration.Short
                )
            }
        }

    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                        scope = scope,
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




