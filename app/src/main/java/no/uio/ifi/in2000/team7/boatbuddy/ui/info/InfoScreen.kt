package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.model.oceanforecast.TimeOceanData


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

    val lat = "58.2" // må hente posisjon fra bruker
    val lon = "6.3025"
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
                var showMessage by remember { mutableStateOf(true) }
                metalertsUIState.metalerts?.features?.forEach {
                    var cardHeight = remember { mutableStateOf(75.dp) }
                    if (showMessage) {
                        var card = NotificationCard(it.awarenessSeriousness,
                            it.consequences,
                            it.description,
                            it.instruction,
                            it.riskMatrixColor,
                            showMessage = showMessage,
                            height = cardHeight.value,
                            onCardClickMax = { cardHeight.value = 250.dp },
                            onCardClickMin = { cardHeight.value = 75.dp },
                            onDismissRequested = { showMessage = false })
                    }
                }
                }


                //NotificationCard(
                //    "Alvorlig situasjon",
                //    "Vær forberedt",
                    //"https://cdn.pixabay.com/photo/2019/06/24/10/42/alpaca-4295702_960_720.jpg",
                  //  "Sterk ising på skip.",
                   // "Fjern is raskt fra båten.",
                   // "#FFA500",
                   // showMessage = showMessage,
                   // onDismissRequested = { showMessage = false }
                // )
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
            .background(Color.Blue)
    ) {
        Column() {
            Text(text = "tid: ${timeLocationData.time}")
            Text(text = "lufttemperatur: ${timeLocationData.air_temperature}")
            Text(text = "vindhastighet: ${timeLocationData.wind_speed}(${timeLocationData.wind_speed_of_gust})") //wave direction?
            Text(text = "tåke: ${timeLocationData.fog_area_fraction}")
        }
    }
}

@Composable
fun NotificationCard( // må etterhvert hente inn posisjon
    awarenessSeriousness: String, consequences: String,
    description: String, resourceInstruction: String,
    riskMatrixColor: String, showMessage: Boolean,
    height: Dp, // Accept dynamic height
    onCardClickMax: () -> Unit,
    onCardClickMin: () -> Unit,// Denne funksjonen vil bli kalt når hele kortet klikkes
    onDismissRequested: () -> Unit) {
    //img: String
    val minimerButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.Black
    )
    val xButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.Black
    )

            Card(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .clickable { onCardClickMax() }
                    .size(width = 500.dp, height = height)
                    .background(Color.Blue)

            )
            {
                Row {
                    Button(
                        onClick = onDismissRequested,
                        modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd), // MÅ FÅ FLYTTA TIL TOPP HØYRE
                        colors = xButtonColor
                    )
                    {
                        Text("X")
                    }
                    Button(
                        onClick = onCardClickMin,
                        modifier = Modifier
                        .wrapContentSize(Alignment.TopStart), // MÅ FÅ FLYTTA TIL TOPP HØYRE
                        colors = minimerButtonColor
                    )
                    {
                        Text("-")
                    }
                }
                Text(
                    text = "$awarenessSeriousness: ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = consequences,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                // Image(
                    // painter = painterResource(id = R.drawable.farevarsel),
                 //   contentDescription = "Icon",
                  //  modifier = Modifier
                    //    .fillMaxWidth()
                    //    .wrapContentSize(Alignment.Center)
                    //    .size(200.dp)
                // )
                Text(
                    text = description,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,

                    )
                Text(
                    text = resourceInstruction,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,

                    )
                // FARGE UTIFRA HVOR FARLIG, GUL, ORANSJE ELLER RØDT
                Text(
                    text = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(android.graphics.Color.parseColor(riskMatrixColor)),
                        )
                )
            }
        }


