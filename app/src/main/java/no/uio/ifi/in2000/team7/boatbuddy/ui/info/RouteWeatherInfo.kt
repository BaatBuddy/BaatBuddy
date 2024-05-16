package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.SaveRouteButton
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun RouteWeatherInfo(
    routeMap: RouteMap,
    locationForecastViewModel: LocationForecastViewModel,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    var loading by remember { mutableStateOf(true) }


    locationForecastViewModel.loadWeekdayForecastRoute(
        routeMap.route.route
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),

        ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = routeMap.mapURL,

                contentDescription = "Map with path",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                onError = {
                    loading = false
                },
                onSuccess = {
                    loading = false
                },
                contentScale = ContentScale.FillWidth

            )




            SaveRouteButton(
                mainViewModel = mainViewModel,
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp),
                profileViewModel = profileViewModel,
                mapboxViewModel = mapboxViewModel,
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
        if (locationForecastUIState.weekdayForecastRoute != null) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                            contentDescription = "beste dager retning",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Beste dager",
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                    Row {
                        Text(
                            text = "Dårligste dager",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowForward,
                            contentDescription = "dårligste dager retning",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                ) {

                    locationForecastUIState.weekdayForecastRoute?.days?.let {
                        it.toList().sortedBy { pair ->
                            pair.second.date
                        }.forEach { tld ->
                            WeatherCard(
                                dayForecast = tld.second,
                                selectedDay = locationForecastUIState.selectedDayRoute,
                                changeDay = { locationForecastViewModel.updateSelectedDayRoute(tld.second) },
                            )

                        }
                    }
                }
            }
            locationForecastUIState.selectedDayRoute?.let {
                WeatherDropDownCard(
                    dayForecast = it,
                    isWeather = true
                )
            }

            locationForecastUIState.selectedDayRoute?.let {
                WeatherDropDownCard(
                    dayForecast = it,
                    isWeather = false
                )
            }
        }
    }


    if (locationForecastUIState.weekdayForecastRoute == null) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
    }

}