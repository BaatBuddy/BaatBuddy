package no.uio.ifi.in2000.team7.boatbuddy.ui.locationforecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LocationForecastScreen(locationForecastViewModel: LocationForecastViewModel = viewModel()) {

    val locationForecastUiState by locationForecastViewModel.locationForecastUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(35.dp)
    ) {

        Text(
            text = locationForecastUiState.locationForecast.toString(),
            modifier = Modifier.verticalScroll(
                rememberScrollState()
            )
        )
    }
}

