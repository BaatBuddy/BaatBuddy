package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.TimeLocationData
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.convertWeatherResId
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.translateSymbolCode


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    metAlertsViewModel: MetAlertsViewModel = viewModel(),
    locationForecastViewModel: LocationForecastViewModel = viewModel(),
    oceanForecastViewModel: OceanForecastViewModel = viewModel(),
    sunriseViewModel: SunriseViewModel = viewModel()
) {

    val metalertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    val oceanForecastUIState by oceanForecastViewModel.oceanForecastUIState.collectAsState()
    val sunriseUIState by sunriseViewModel.sunriseUIState.collectAsState()

    val lat = "59.9" // må hente posisjon fra bruker
    val lon = "10.7"



    metAlertsViewModel.initialize(lat = lat, lon = lon)
    locationForecastViewModel.initialize(lat = lat, lon = lon)
    oceanForecastViewModel.initialize(lat = lat, lon = lon)
    sunriseViewModel.initialize(lat = lat, lon = lon)

    Scaffold(
        modifier = Modifier
            .padding(8.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            val selectedDay =
                remember {
                    locationForecastUIState.weekdayForecast?.days?.let {
                        mutableStateOf(
                            it.firstNotNullOf { day -> day.value })
                    }
                }

            Column {
                Text(
                    text = "Været for de neste dagene:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {

                    locationForecastUIState.weekdayForecast?.days?.let {
                        it.toList().sortedBy { pair ->
                            pair.second.date
                        }.forEach { tld ->
                            if (selectedDay != null) {
                                LocationCard(
                                    dayForecast = tld.second,
                                    selectedDay
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    if (selectedDay != null) {
                        LocationTable(dayForecast = selectedDay)
                    }
                }

            }
        }
    }
}

@Composable
fun LocationCard(
    dayForecast: DayForecast,
    selectedDay: MutableState<DayForecast>
) {
    val timeLocationData = dayForecast.middayWeatherData
    Card(
        modifier = Modifier
            .padding(4.dp),
        onClick = { selectedDay.value = dayForecast },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            if (selectedDay.value == dayForecast) Color(0xFFCCCCCC) else Color(
                0xFFE1E2EC
            )
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = dayForecast.day)
            Text(text = "${timeLocationData.air_temperature}℃")
            WeatherIcon(
                symbolCode = timeLocationData.symbol_code,
                modifier = Modifier
                    .fillMaxSize(0.15f)
            )

            val symbolCode = translateSymbolCode(timeLocationData.symbol_code)
//            if (showMore) {
//                Text(text = symbolCode)
//
//                Text(text = "vindhastighet: ${timeLocationData.wind_speed}(${timeLocationData.wind_speed_of_gust})")
//                Text(text = "tåke: ${timeLocationData.fog_area_fraction}")
//            }
        }


    }

}

@Composable
fun LocationTable(dayForecast: MutableState<DayForecast>) {
    val df = dayForecast.value
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = df.day,
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
            modifier =
            Modifier.padding(bottom = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Tid")
            Text(text = "Temperatur")
            Text(text = "Nedbør")
            Text(text = "Vind(kast)")
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            df.weatherData.forEach {
                HorizontalDivider()
                LocationRow(tld = it)
            }
        }
    }
}

@Composable
fun LocationRow(tld: TimeLocationData) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = tld.time.substring(11, 13))
        Row {
            WeatherIcon(
                symbolCode = tld.symbol_code,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = tld.air_temperature.toString() + "°",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(text = tld.precipitation_amount.toString())
        Text(text = "${tld.wind_speed}(${tld.wind_speed_of_gust})")
    }
}


@Composable
fun WeatherIcon(symbolCode: String, modifier: Modifier) {
    Icon(
        painter = painterResource(
            id = convertWeatherResId(
                symbolCode,
                context = LocalContext.current
            )
        ),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier

    )
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
            text = "$awarenessSeriousness ", // CHANGED BASED ON METALERT CHANGE
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
        )
        Text(
            text = "$consequences",
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


