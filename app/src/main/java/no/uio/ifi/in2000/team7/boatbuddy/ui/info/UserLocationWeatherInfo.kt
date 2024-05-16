package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel

@Composable
fun UserLocationWeatherInfo(
    locationForecastViewModel: LocationForecastViewModel,
    userLocationViewModel: UserLocationViewModel,
) {
    val locationForecastUIState by locationForecastViewModel.locationForecastUIState.collectAsState()
    val userLocationUIState by userLocationViewModel.userLocationUIState.collectAsState()

    if (userLocationUIState.userLocation == null) {
        userLocationViewModel.fetchUserLocation()
    } else {
        locationForecastViewModel.loadWeekdayForecastUser(
            userLocationUIState.userLocation!!
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        if (userLocationUIState.userLocation != null) {
            if (locationForecastUIState.weekdayForecastUser != null) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
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
                            .horizontalScroll(rememberScrollState())
                    ) {

                        locationForecastUIState.weekdayForecastUser?.days?.let {
                            it.toList().sortedBy { pair ->
                                pair.second.date
                            }.forEach { tld ->
                                WeatherCard(
                                    dayForecast = tld.second,
                                    selectedDay = locationForecastUIState.selectedDayUser,
                                    changeDay = {
                                        locationForecastViewModel.updateSelectedDayUser(
                                            tld.second
                                        )
                                    },
                                )

                            }
                        }
                    }
                }
                locationForecastUIState.selectedDayUser?.let {
                    WeatherDropDownCard(
                        dayForecast = it,
                        isWeather = true
                    )
                }
                locationForecastUIState.selectedDayUser?.let {
                    WeatherDropDownCard(
                        dayForecast = it,
                        isWeather = false
                    )
                }
            }

        }
    }

    // TODO check if user has given position
    if (locationForecastUIState.weekdayForecastUser == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
    }


}