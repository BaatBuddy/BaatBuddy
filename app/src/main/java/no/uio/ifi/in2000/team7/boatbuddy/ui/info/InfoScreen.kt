package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
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
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    // infoScreenViewModel: InfoScreenViewModel = viewModel(),
    metAlertsViewModel: MetAlertsViewModel = viewModel(),
    locationForecastViewModel: LocationForecastViewModel = viewModel(),
    oceanForecastViewModel: OceanForecastViewModel = viewModel(),
    sunriseViewModel: SunriseViewModel = viewModel()
) {

    // val weatherUIState by infoScreenViewModel.weatherUIState.collectAsState()
    val metalertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    val oceanForecastUIState by oceanForecastViewModel.oceanForecastUIState.collectAsState()
    val sunriseUIState by sunriseViewModel.sunriseUIState.collectAsState()

    val lat = "59.9"
    val lon = "10.1"
    metAlertsViewModel.initialize(lat = lat, lon = lon)
    locationForecastViewModel.initialize(lat = lat, lon = lon)
    oceanForecastViewModel.initialize(lat = lat, lon = lon)
    sunriseViewModel.initialize(lat = lat, lon = lon)
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
                    if (oceanForecastUIState.oceanForecast?.timeseries?.isNotEmpty() == true) {
                        oceanForecastUIState.oceanForecast?.timeseries?.let { timeOceanData ->
                            OceanCard(
                                modifier = modifier,
                                timeOceanData = timeOceanData[1]
                            )
                        }
                    }
                }

                Column {
                    Text(text = "Dette er værdata")
                    locationForecastUIState.locationForecast?.timeseries?.let { timeLocationData ->
                        LocationCard(
                            modifier = modifier,
                            timeLocationData = timeLocationData[1]
                        )
                    }

                }


            }

            Column {
                Text(text = "Dette er farevarsler")
                metalertsUIState.metalerts?.features?.forEach {
                    AlertCard(modifier = modifier, feature = it)
                }
                Text(text = "Dette er soldata")
                if (sunriseUIState.sunriseData != null) {
                    sunriseUIState.sunriseData?.sunriseTime?.let { it1 -> Text(text = it1) }
                    sunriseUIState.sunriseData?.sunriseTime?.let { it1 -> Text(text = it1) }
                }
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {

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
            Text(text = "vindhastighet: ${timeLocationData.wind_speed}(${timeLocationData.wind_speed_of_gust})") //wave direction?
            Text(text = "tåke: ${timeLocationData.fog_area_fraction}")
        }
    }
}


