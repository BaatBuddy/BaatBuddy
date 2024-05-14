package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.NetworkConnectivityObserver
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
    status: NetworkConnectivityObserver.Status
) {

    // fetches all alerts (no arguments)
    metalertsViewModel.initialize()

    mainViewModel.selectScreen(0)

    val metAlertsUIState by metalertsViewModel.metalertsUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val homeScreenUIState by homeViewModel.homeScreenUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUIState.collectAsState()

    val context = LocalContext.current

    /*LaunchedEffect(status) {
        Log.d("InternetStatus", "$status")
    }*/

    // Initialize map
    if (status == NetworkConnectivityObserver.Status.Available) {
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
            FloatingMapButtons(
                homeViewModel = homeViewModel,
                mapboxViewModel = mapboxViewModel,
                locationForecastViewModel = locationForecastViewModel,
            )
        }

    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            if (mapboxUIState.mapView != null) { // mapboxUIState.mapView can only be null if we didn't have internet access upon opening the app
                AndroidView(
                    factory = { _ ->
                        mapboxUIState.mapView!! // Her lages kartet
                    }
                )
            }

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
                        metalertsViewModel = metalertsViewModel,
                        infoScreenViewModel = infoScreenViewModel,
                        homeViewModel = homeViewModel,
                        locationForecastViewModel = locationForecastViewModel,
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
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }


        }
    }

    if (homeScreenUIState.showExplanationCard) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            ExplanationCard(
                homeViewModel = homeViewModel
            )
        }
    }

    if (homeScreenUIState.showWeatherAlertInfoCard != null) {
        WeatherAlertInfoCard(
            homeViewModel = homeViewModel,
            featureData = homeScreenUIState.showWeatherAlertInfoCard!!
        )
    }
}

