package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.WeatherIcon
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun SwipeUpContent(
    weekdayForecastRoute: WeekForecast?,
    profileViewModel: ProfileViewModel,
    mainViewModel: MainViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavController,
    infoScreenViewModel: InfoScreenViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    metalertsViewModel: MetAlertsViewModel,
) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val metalertsUIState by metalertsViewModel.metalertsUIState.collectAsState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = "Din reise",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )
        // weather alerts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            //alerts
            metalertsUIState.alerts.forEach {
                AlertIcon(
                    event = it.event,
                    riskMatrixColor = it.riskMatrixColor,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )
            }
            VerticalDivider(
                thickness = 2.dp,
                modifier = Modifier
                    .padding(8.dp)
            )
            // save route
            Button(onClick = {
                if (profileUIState.selectedUser != null && profileUIState.selectedBoat != null && mapboxUIState.routeData is APIStatus.Success) {
                    profileViewModel.updateCurrentRoute(mapboxUIState.generatedRoute?.route?.route)
                    navController.navigate("saveroute")
                    mainViewModel.hideBottomBar()
                } else {
                    Log.d("SwipeUpContent", "Ingen bruker lagret")
                    mainViewModel.showNoUserDialog()
                }
            }) {
                Text(text = "Lagre rute")
            }
        }

        if (weekdayForecastRoute != null) {
            DayWeatherTable(
                weekForecast = weekdayForecastRoute,
                navController = navController,
                infoScreenViewModel = infoScreenViewModel,
                profileViewModel = profileViewModel,
                mapboxViewModel = mapboxViewModel,
                homeViewModel = homeViewModel,
                locationForecastViewModel = locationForecastViewModel,
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun DayWeatherTable(
    weekForecast: WeekForecast,
    navController: NavController,
    infoScreenViewModel: InfoScreenViewModel,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Dag")
            Text(text = "Temperatur")
            Text(text = "Score")
            Text(text = "")
        }
        weekForecast.days.toList().map { it.second }.sortedBy { it.date }.forEach { dayForecast ->
            HorizontalDivider(
                modifier = Modifier
                    .padding(2.dp)
            )
            DayWeatherRow(
                dayForecast = dayForecast,
                navController = navController,
                infoScreenViewModel = infoScreenViewModel,
                profileViewModel = profileViewModel,
                mapboxViewModel = mapboxViewModel,
                homeViewModel = homeViewModel,
                locationForecastViewModel = locationForecastViewModel
            )
        }
    }
}

@Composable
fun DayWeatherRow(
    dayForecast: DayForecast,
    navController: NavController,
    infoScreenViewModel: InfoScreenViewModel,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel
) {
    val middayWeatherData = dayForecast.middayWeatherData

    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .clickable {
                // navigate to weather info screen with route selected
                profileViewModel.updatePickedRoute(mapboxUIState.generatedRoute)
                infoScreenViewModel.selectTab(1)
                homeViewModel.hideBottomSheet()
                locationForecastViewModel.updateSelectedDayRoute(dayForecast = dayForecast)

                navController.navigate(Screen.InfoScreen.route) {

                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }

            }
    ) {
        Text(text = dayForecast.day)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherIcon(
                symbolCode = middayWeatherData.symbolCode,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(text = "${middayWeatherData.airTemperature}℃")
        }
        Text(
            text = "%.1f".format(dayForecast.dayScore?.score),
            color = WeatherScore.getColor(dayForecast.dayScore?.score!!)
        )
        Text(text = ">  ")

    }
}












