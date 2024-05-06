package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.DayWeatherTable
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel

@Composable
fun UserLocationWeatherInfo(
    locationForecastViewModel: LocationForecastViewModel,
    userLocationViewModel: UserLocationViewModel,
) {
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    val userLocationUIState by userLocationViewModel.userLocationUIState.collectAsState()

    if (userLocationUIState.userLocation == null) {
        userLocationViewModel.fetchUserLocation()
    } else if (!locationForecastUIState.fetchedWeekDayUser) {
        locationForecastViewModel.loadWeekdayForecastUser(
            userLocationUIState.userLocation!!
        )
    }

    Column {
        if (userLocationUIState.userLocation != null) {
            if (locationForecastUIState.weekdayForecastUser != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {

                    locationForecastUIState.weekdayForecastUser?.days?.let {
                        it.toList().sortedBy { pair ->
                            pair.second.date
                        }.forEach { tld ->
                            LocationCard(
                                dayForecast = tld.second,
                                selectedDay = locationForecastUIState.selectedDayUser,
                                changeDay = { locationForecastViewModel.updateSelectedDayUser(tld.second) },
                            )

                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    locationForecastUIState.selectedDayUser?.let { LocationTable(dayForecast = it) }
                }
            }

        }
    }

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