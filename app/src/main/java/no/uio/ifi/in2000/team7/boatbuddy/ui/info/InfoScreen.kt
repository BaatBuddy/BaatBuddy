package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
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
    sunriseViewModel: SunriseViewModel = viewModel(),
    autorouteViewModel: AutoRouteViewModel = viewModel()
) {

    // val weatherUIState by infoScreenViewModel.weatherUIState.collectAsState()
    val metalertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    val oceanForecastUIState by oceanForecastViewModel.oceanForecastUIState.collectAsState()
    val sunriseUIState by sunriseViewModel.sunriseUIState.collectAsState()
    val autorouteUiState by autorouteViewModel.autoRouteUiState.collectAsState()

    val lat = "59.9" // må hente posisjon fra bruker
    val lon = "10.7"
    val boatSpeed = "5"
    val boatHeight = "5"
    val safetyDepth = "5"
    val course = listOf<Point>(
        Point.fromLngLat(10.607909077722013, 59.856108702935046),
        Point.fromLngLat(10.607421841197663, 59.860213589239464)
    )


    metAlertsViewModel.initialize(lat = lat, lon = lon)
    locationForecastViewModel.initialize(lat = lat, lon = lon)
    oceanForecastViewModel.initialize(lat = lat, lon = lon)
    sunriseViewModel.initialize(lat = lat, lon = lon)
    autorouteViewModel.initialize(course, boatSpeed, boatHeight, safetyDepth)

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
                    val cardHeight = remember { mutableStateOf(75.dp) }
                    if (showMessage) {
                        var card = it.awarenessSeriousness?.let { it1 ->
                            it.consequences?.let { it2 ->
                                NotificationCard(
                                    it1,
                                    it2,
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
                }
            }
            var showMessage by remember { mutableStateOf(true) }
            val cardHeight = remember { mutableStateOf(75.dp) }
            NotificationCard(
                "Utfordrende situasjon",
                "Middels høye bølger. Bølgekammene er ved å brytes opp til sjørokk.",
            "Nordøst sterk kuling 20 m/s.",
              "Ikke dra ut i småbåt.",
             "Yellow",
                showMessage = showMessage,
                height = cardHeight.value,
                onCardClickMax = { cardHeight.value = 250.dp },
                onCardClickMin = { cardHeight.value = 75.dp },
                onDismissRequested = { showMessage = false }
            )
            Text(text = "Dette er soldata")
            if (sunriseUIState.sunriseData != null) {
                sunriseUIState.sunriseData?.sunriseTime?.let { it1 -> Text(text = it1) }
                sunriseUIState.sunriseData?.sunriseTime?.let { it1 -> Text(text = it1) }
            }

            Text(text = "Dette er autoroute")
            if (autorouteUiState.autoRoute != null) {
                autorouteUiState.autoRoute?.geometry?.coordinates.let { it1 -> Text(text = it1.toString()) }
                
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
//            Text(text = "konsekvenser: ${feature.consequences}")

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
    awarenessSeriousness: String,
    consequences: String,
    description: String, resourceInstruction: String,
    riskMatrixColor: String, showMessage: Boolean,
    height: Dp, // Accept dynamic height
    onCardClickMax: () -> Unit,
    onCardClickMin: () -> Unit,// Denne funksjonen vil bli kalt når hele kortet klikkes
    onDismissRequested: () -> Unit
) {
    //img: String
    val minimerButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.Red
    )
    val xButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.Red
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
        if (awarenessSeriousness != null) {
        Text(
            text = "$awarenessSeriousness ", // CHANGED BASED ON METALERT CHANGE
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
        )
        }
        if (consequences != null) {
            Text(
                text = "$consequences",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
        // Image(
        // painter = painterResource(id = R.drawable.farevarsel),
        //   contentDescription = "Icon",
        //  modifier = Modifier
        //    .fillMaxWidth()
        //    .wrapContentSize(Alignment.Center)
        //    .size(200.dp)
        // )
        if (description != null) {
            Text(
                text = description,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
        if (resourceInstruction != null) {
            Text(
                text = resourceInstruction, // må endres til instruction
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,

                )
        }
        // FARGE UTIFRA HVOR FARLIG, GUL, ORANSJE ELLER RØDT
        if (riskMatrixColor != null) {
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
}


