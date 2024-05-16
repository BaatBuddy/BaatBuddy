package no.uio.ifi.in2000.team7.boatbuddy.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowFinishDialog
import no.uio.ifi.in2000.team7.boatbuddy.model.dialog.Dialog.ShowStartDialog
import no.uio.ifi.in2000.team7.boatbuddy.model.internet.Status
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.DeleteRouteDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.LocationDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.NoUserDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.NotificationDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.StartTrackingDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs.StopTrackingDialog
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxUIState
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding.OnBoarding
import no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding.OnBoardingViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateBoatScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateUserScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.SelectBoatScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.SelectWeatherScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.route.RouteInfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.route.RouteScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.route.SaveRouteScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    mapboxViewModel: MapboxViewModel,
    metalertsViewModel: MetAlertsViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    userLocationViewModel: UserLocationViewModel,
    networkConnectivityViewModel: NetworkConnectivityViewModel,
    onBoardingViewModel: OnBoardingViewModel,
    activity: ComponentActivity,
) {
    val context = LocalContext.current

    // Internet connectivity
    val status by networkConnectivityViewModel.connectionUIState.collectAsState()

    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    val routeUIState by profileViewModel.routeScreenUIState.collectAsState()
    val onBoardingUIState by onBoardingViewModel.onBoardingUIState.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    if (onBoardingUIState.showOnBoarding) {
        OnBoarding(
            onBoardingViewModel = onBoardingViewModel,
            profileViewModel = profileViewModel,
            navController = navController,
        )
    } else {

        // Observe Internet connection, initialize map, show snackbars
        InitializeMap(status = status, mapboxViewModel = mapboxViewModel, context = context)
        ShowSnackbars(
            scope = scope,
            snackbarHostState = snackbarHostState,
            status = status,
            mapboxUIState = mapboxUIState
        )

        if (!mainScreenUIState.launched) {
            when {
                ContextCompat.checkSelfPermission(
                    activity as Context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    mapboxViewModel.panToUser()
                    Log.i("ASDASD", "first")

                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    Log.i("ASDASD", "thing")
                    mainViewModel.showLocationDialog()
                }

                else -> {
                    Log.i("ASDASD", "Else")

                    mainViewModel.showLocationDialog()
                }
            }

            mainViewModel.updateLaunched(true)
        }

        // If user does not have internet access, show snackbar
        LaunchedEffect(status) {
            if (status == Status.LOST || status == Status.UNAVAILABLE || status == Status.LOSING) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Du er ikke koblet til Internett",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        // if route is either too long or points is not close enough to the water
        LaunchedEffect(mapboxUIState.lastRouteData) {
            if (mapboxUIState.routeData is APIStatus.Failed
                && mapboxUIState.lastRouteData is APIStatus.Loading
            ) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = if (status == Status.AVAILABLE) {
                            "Ruten er for lang eller inneholder punkter på land"
                        } else {
                            "Kan ikke generere rute uten tilgang til Internett"
                        },
                        duration = SnackbarDuration.Short
                    )
                }
            }

        }

        // notification setup
        val settingsActivityResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { _ ->
        }

        // Show the dialog if required

        // Show the Notification dialog if required
        if (mainScreenUIState.showNotificationDialog && !NotificationManagerCompat.from(LocalContext.current)
                .areNotificationsEnabled()
        ) {
            NotificationDialog(
                navigateToSettings = {
                    mainViewModel.navigateToNotificationSettings()
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
                onDismiss = {
                    mainViewModel.hideNotificationDialog()
                }
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                if (it) {
                    userLocationViewModel.requestLocationPermission()
                    userLocationViewModel.fetchUserLocation()
                    mapboxViewModel.panToUser()
                }
            }

        // Shows a dialog for location permissions
        if (mainScreenUIState.showLocationDialog) {
            LocationDialog(
                launchRequest = {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                onDismiss = { mainViewModel.hideLocationDialog() }
            )
        }

        // Shows if user has no selected profile
        if (mainScreenUIState.showNoUserDialog) {
            NoUserDialog(
                onDismissRequest = {
                    mainViewModel.hideNoUserDialog()
                },
                onConfirmation = {
                    mainViewModel.selectScreen(4)

                    navController.navigate(Screen.ProfileScreen.route) {

                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                    mainViewModel.hideNoUserDialog()

                },
                dialogTitle = "Ingen bruker valgt",
                dialogText = "Du må lage eller velge en bruker",
                icon = Icons.Default.Info,


                )

        }

        if (mainScreenUIState.showDeleteRouteDialog) {
            DeleteRouteDialog(
                onDismissRequest = {
                    mainViewModel.updateShowDeleteRouteDialog(false)
                },
                onConfirmation = {
                    profileViewModel.deleteSelectedRoute()
                    mainViewModel.updateShowDeleteRouteDialog(false)
                    navController.popBackStack()
                },
                dialogTitle = "Har du lyst til å slette ${routeUIState.selectedRouteMap?.route?.routename}?",
                dialogText = "Ruten vil bli slettet for alltid!",
                icon = Icons.Default.Delete
            )
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
                            profileViewModel = profileViewModel,
                            infoScreenViewModel = infoScreenViewModel,
                            networkConnectivityViewModel = networkConnectivityViewModel,
                        )
                    }
                    composable(route = Screen.InfoScreen.route) {
                        InfoScreen(
                            locationForecastViewModel = locationForecastViewModel,
                            mainViewModel = mainViewModel,
                            infoScreenViewModel = infoScreenViewModel,
                            userLocationViewModel = userLocationViewModel,
                            profileViewModel = profileViewModel,
                            mapboxViewModel = mapboxViewModel,
                            navController = navController,
                            networkConnectivityViewModel = networkConnectivityViewModel
                        )
                    }
                    composable(route = Screen.ProfileScreen.route) {
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
                    composable(route = "routeinfo") {
                        RouteInfoScreen(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            profileViewModel = profileViewModel,
                            locationForecastViewModel = locationForecastViewModel,

                            )
                    }
                    composable(route = "saveroute") {
                        SaveRouteScreen(
                            profileViewModel = profileViewModel,
                            navController = navController,
                            mainViewModel = mainViewModel,
                            mapboxViewModel = mapboxViewModel

                        )
                    }
                    composable(route = "selectboat") {
                        SelectBoatScreen(
                            profileViewModel = profileViewModel,
                            navController = navController
                        )
                    }
                    composable(route = "selectweather") {
                        SelectWeatherScreen(
                            profileViewModel = profileViewModel,
                            navController = navController,
                            mainViewModel = mainViewModel,
                        )
                    }
                }
                if (mainScreenUIState.showDialog == ShowStartDialog) {
                    // If we have location
                    StartTrackingDialog(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        userLocationViewModel = userLocationViewModel
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

}

@Composable
fun InitializeMap(
    status: Status,
    mapboxViewModel: MapboxViewModel,
    context: Context,
) {

    if (status == Status.AVAILABLE) {
        mapboxViewModel.initialize(
            context = context,
            cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(9.0, 61.5))
                .zoom(4.0)
                .bearing(0.0)
                .pitch(0.0)
                .build()
        )
    }

}

@Composable
fun ShowSnackbars(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    status: Status,
    mapboxUIState: MapboxUIState,
) {

    // If user does not have internet access, show snackbar
    LaunchedEffect(status) {
        if (status == Status.UNAVAILABLE || status == Status.LOST || status == Status.LOSING) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Du er ikke koblet til Internett",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    // if route is either too long or points is not close enough to the water
    LaunchedEffect(mapboxUIState.lastRouteData) {
        if (mapboxUIState.routeData is APIStatus.Failed
            && mapboxUIState.lastRouteData is APIStatus.Loading
        ) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = if (status == Status.AVAILABLE) {
                        "Ruten er for lang eller inneholder punkter på land"
                    } else {
                        "Kan ikke generere rute uten tilgang til Internett"
                    },
                    duration = SnackbarDuration.Short
                )
            }
        }

    }


}


