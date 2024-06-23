package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.main.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.route.SaveRouteButton
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun SwipeUpContent(
    weekdayForecastRoute: WeekForecast?,
    profileViewModel: ProfileViewModel,
    mainViewModel: MainViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavController,
    metalertsViewModel: MetAlertsViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel,
) {
    val metalertsUIState by metalertsViewModel.metalertsUIState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "De beste dagene for din reise",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        )

        // weather alerts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //alerts
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                metalertsUIState.alerts.forEach { featureData ->
                    IconButton(
                        onClick = {
                            homeViewModel.updateShowWeatherAlertInfoCard(featureData)
                        },
                        modifier = Modifier
                            .height(IntrinsicSize.Max)
                            .clip(RoundedCornerShape(100))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            AlertIcon(
                                event = featureData.event,
                                riskMatrixColor = featureData.riskMatrixColor,
                                modifier = Modifier
                                    .fillMaxSize(0.75f)
                            )

                        }

                    }
                }
            }
            VerticalDivider(
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            SaveRouteButton(
                mainViewModel = mainViewModel,
                navController = navController,
                modifier = Modifier
                    .padding(8.dp),
                profileViewModel = profileViewModel,
                mapboxViewModel = mapboxViewModel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        if (weekdayForecastRoute != null) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                            contentDescription = "beste dager retning",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = "Beste dager",
                            color = MaterialTheme.colorScheme.primaryContainer
                        )

                    }
                    Row {
                        Text(
                            text = "Dårligste dager",
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowForward,
                            contentDescription = "dårligste dager retning",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp)
                ) {
                    weekdayForecastRoute.days.values.sortedByDescending { it.dayScore?.score }
                        .forEach { dayForecast ->
                            NavigatingWeatherCard(
                                dayForecast = dayForecast,
                                profileViewModel = profileViewModel,
                                mapboxViewModel = mapboxViewModel,
                                infoScreenViewModel = infoScreenViewModel,
                                homeViewModel = homeViewModel,
                                locationForecastViewModel = locationForecastViewModel,
                                navController = navController
                            )
                        }
                }
            }


        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}

/*
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
*/


