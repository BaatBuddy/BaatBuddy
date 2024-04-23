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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.convertWeatherResId


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
                            LocationCard(
                                dayForecast = tld.second,
                                selectedDay = locationForecastUIState.selectedDay,
                                changeDay = { locationForecastViewModel.updateSelectedDay(tld.second) },
                            )

                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    locationForecastUIState.selectedDay?.let { LocationTable(dayForecast = it) }
                }

            }
        }
    }
}


@Composable
fun LocationCard(
    dayForecast: DayForecast,
    selectedDay: DayForecast?,
    changeDay: () -> Unit
) {
    val timeLocationData = dayForecast.middayWeatherData
    Card(
        modifier = Modifier
            .padding(4.dp),
        onClick = changeDay,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            if (selectedDay == dayForecast) Color(0xFFCCCCCC) else Color(
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

//            val symbolCode = translateSymbolCode(timeLocationData.symbol_code)
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
fun LocationTable(dayForecast: DayForecast) {
    val df = dayForecast
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = df.day,
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(text = "Tid")
            Text(text = "Temperatur")
            Text(text = "Nedbør")
            Text(text = "Vind(kast)")
        }
        LazyColumn {
            items(df.weatherData) { tld ->
                val nextItem = df.weatherData.zipWithNext().firstOrNull { pair ->
                    pair.first == tld
                }?.second
                var time = tld.time.substring(11, 13)

                time = if (nextItem != null) {
                    val nextItemTime = nextItem.time.substring(11, 13)
                    when {
                        (time == "00" && nextItemTime != "01") -> "00 - 06"
                        (time == "06" && nextItemTime != "07") -> "06 - 12"
                        (time == "12" && nextItemTime != "13") -> "12 - 18"
                        else -> time
                    }
                } else {
                    if (time != "23") {
                        when (time) {
                            "00" -> "00 - 06"
                            "06" -> "06 - 12"
                            "12" -> "12 - 18"
                            "18" -> "18 - 00"
                            else -> "18 - 00"
                        }
                    } else {
                        time
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(
                        text = time
                    )

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

                    Text(
                        text = tld.precipitation_amount.toString()
                    )

                    Text(
                        text = "${tld.wind_speed}${if (tld.wind_speed_of_gust != 0.0) "(${tld.wind_speed_of_gust})" else ""}"
                    )
                }
            }
        }
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


