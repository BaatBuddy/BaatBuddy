package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel

@Composable
fun UserLocationWeatherInfo(
    locationForecastViewModel: LocationForecastViewModel,
    userLocationViewModel: UserLocationViewModel,
) {
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    val userLocationUIState by userLocationViewModel.userLocationUIState.collectAsState()



    Column {
        Text(text = userLocationUIState.userLocation.toString())
        Button(onClick = { userLocationViewModel.getFineLocation() }) {
            Text(text = "Update location")
        }
    }

}