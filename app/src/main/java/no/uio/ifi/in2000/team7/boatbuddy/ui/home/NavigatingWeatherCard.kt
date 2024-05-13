package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.ScoreToEmoji
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.WeatherIcon
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun NavigatingWeatherCard(
    dayForecast: DayForecast,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    navController: NavController
) {
    val emojiString = ScoreToEmoji(dayForecast.dayScore?.score) ?: ""
    val timeLocationData = dayForecast.middayWeatherData
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    Card(
        modifier = Modifier
            .padding(4.dp),
        onClick = {
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
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        // TODO fix colors
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = dayForecast.day)
            Text(text = "${timeLocationData.airTemperature}℃")
            WeatherIcon(
                symbolCode = timeLocationData.symbolCode,
                modifier = Modifier
                    .fillMaxSize(0.15f)
            )

            Text(
                text = emojiString,
                fontSize = 30.sp

            )
        }
    }
}
