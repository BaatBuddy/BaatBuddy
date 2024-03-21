package no.uio.ifi.in2000.team7.boatbuddy.ui.test.oceanforecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun OceanForecastScreen(oceanForecastViewModel: OceanForecastViewModel = viewModel()) {

    val oceanForecastUIState by oceanForecastViewModel.oceanForecastUIState.collectAsState()

    // filler data
    var lon by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }


    Column {
        Text(
            text = oceanForecastUIState.oceanForecastData.toString(),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
        TextField(value = lon, onValueChange = { lon = it }, label = { Text(text = "lon") })
        TextField(value = lat, onValueChange = { lat = it }, label = { Text(text = "lat") })
        Button(onClick = { oceanForecastViewModel.initialize(lat, lon) }) {

        }
    }
}