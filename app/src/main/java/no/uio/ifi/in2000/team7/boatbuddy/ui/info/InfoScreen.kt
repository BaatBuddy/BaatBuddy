package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.ui.locationforecast.InfoScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    navController: NavController,
    infoScreenViewModel: InfoScreenViewModel = viewModel()
) {

    val weatherUIState by infoScreenViewModel.weatherUIState.collectAsState()

    infoScreenViewModel.initialize("59.9", "10.7", "0")
    Scaffold(

    ) {
        Row {
            Column {
                Text(text = "Dette er havdata")
                weatherUIState.oceanForecast?.timeseries?.let { timeOceanData ->
                    OceanCard(
                        modifier = Modifier,
                        timeLocationData = timeOceanData[0]
                    )
                }

            }
            Column {

            }
        }

        Column {
            weatherUIState.metAlerts?.features?.forEach {
                AlertCard(modifier = Modifier, feature = it)
            }
        }
    }

}


@Composable
fun AlertCard(modifier: Modifier, feature: FeatureData) {
    Card {
        Column {
            Text(text = "start: ${feature.start}")
            Text(text = "slutt: ${feature.end}")
            Text(text = "konsekvenser: ${feature.consequences}")

        }

    }

}

@Composable
fun OceanCard(modifier: Modifier, timeLocationData: TimeLocationData) {
    Card {
        Column {
            Text(text = "tid: ${timeLocationData.time}")
            Text(text = "bølgehøyde: ${timeLocationData.sea_surface_wave_height}")
            Text(text = "bølgeretning: ${timeLocationData.sea_surface_wave_from_direction}") //wave direction?
            Text(text = "strømhastighet: ${timeLocationData.sea_water_speed}")
        }
    }
}