package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    infoScreenViewModel: InfoScreenViewModel = viewModel()
) {

    val weatherUIState by infoScreenViewModel.weatherUIState.collectAsState()

    infoScreenViewModel.initialize("59.9", "10.7", "0")
    Scaffold(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val modifier = Modifier
                .padding(8.dp)
            Row(

            ) {

                Column {
                    Text(text = "Dette er havdata")
                    weatherUIState.oceanForecast?.timeseries?.let { timeOceanData ->
                        OceanCard(
                            modifier = modifier,
                            timeOceanData = timeOceanData[1]
                        )
                    }
                }

                Column {
                    Text(text = "Dette er værdata")
                    weatherUIState.locationForecast?.timeseries?.let { timeLocationData ->
                        LocationCard(
                            modifier = modifier,
                            timeLocationData = timeLocationData[1]
                        )
                    }

                }


            }
            Text(text = "Dette er farevarsler")
            Column {
                weatherUIState.metAlerts?.features?.forEach {
                    AlertCard(modifier = modifier, feature = it)
                }
            }
        }
    }

}


@Composable
fun AlertCard(modifier: Modifier, feature: FeatureData) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
        ) {
            Text(text = "start: ${feature.start}")
            Text(text = "slutt: ${feature.end}")
            Text(text = "konsekvenser: ${feature.consequences}")

        }

    }

}

@Composable
fun OceanCard(modifier: Modifier, timeOceanData: TimeOceanData) {
    Card() {
        Column() {
            Text(text = "tid: ${timeOceanData.time}")
            Text(text = "bølgehøyde: ${timeOceanData.sea_surface_wave_height}")
            Text(text = "bølgeretning: ${timeOceanData.sea_surface_wave_from_direction}") //wave direction?
            Text(text = "strømhastighet: ${timeOceanData.sea_water_speed}")
        }
    }
}

@Composable
fun LocationCard(modifier: Modifier, timeLocationData: TimeLocationData) {
    Card(
        modifier = modifier
    ) {
        Column() {
            Text(text = "tid: ${timeLocationData.time}")
            Text(text = "lufttemperatur: ${timeLocationData.air_temperature}")
            Text(text = "bølgeretning: ${timeLocationData.wind_speed}(${timeLocationData.wind_speed_of_gust})") //wave direction?
            Text(text = "strømhastighet: ${timeLocationData.fog_area_fraction}")
        }
    }
}


