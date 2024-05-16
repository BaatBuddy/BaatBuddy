package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel

@Composable
fun UserLocationWeatherInfo(
    locationForecastViewModel: LocationForecastViewModel,
    userLocationViewModel: UserLocationViewModel,
) {
    val locationForecastUIState by locationForecastViewModel.locationForecastUIState.collectAsState()
    val userLocationUIState by userLocationViewModel.userLocationUIState.collectAsState()
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) {
                userLocationViewModel.requestLocationPermission()
                userLocationViewModel.fetchUserLocation()
            }
        }

    if (userLocationUIState.userLocation == null) {
        userLocationViewModel.fetchUserLocation()
    } else {
        locationForecastViewModel.loadWeekdayForecastUser(
            userLocationUIState.userLocation!!
        )
    }

    if (userLocationUIState.userLocation != null) {
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
    } else {
        Scaffold(
        ) { paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                Column {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_location_off_24),
                            contentDescription = "Location Icon",
                            modifier = Modifier.size(120.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Vi trenger din posisjon",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "For å kunne vise værforholdene i ditt område, trenger vi din posisjon. \n Trykk på knappen nedenfor for å dele posisjon.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                Log.d("UserLocation", "$userLocationUIState.userLocation")
                            }
                        ) {
                            Text(text = "Del posisjon")
                        }
                    }
                }
            }
        }
    }

}