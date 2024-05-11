package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertWeatherResId
import no.uio.ifi.in2000.team7.boatbuddy.data.weathercalculator.WeatherScore
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    locationForecastViewModel: LocationForecastViewModel,
    mainViewModel: MainViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    userLocationViewModel: UserLocationViewModel,
    profileViewModel: ProfileViewModel,
) {

    val infoScreenUIState by infoScreenViewModel.infoScreenUIState.collectAsState()
    val routeScreenUIState by profileViewModel.routeScreenUIState.collectAsState()
    val profileUIState by profileViewModel.profileUIState.collectAsState()

    if (profileUIState.updateWeather) {
        locationForecastViewModel.updateScore()
        profileViewModel.stopUpdateWeather()
    }

    mainViewModel.selectScreen(1)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Været", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)


        ) {
            Column {
                TabRow(
                    selectedTabIndex = infoScreenUIState.selectedTab,
                    modifier = Modifier
                        .fillMaxWidth(),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[infoScreenUIState.selectedTab])
                        )
                    }
                ) {
                    val tabTitles = listOf("For din posisjon", "For din rute")

                    tabTitles.forEachIndexed { index, title ->
                        val selected = infoScreenUIState.selectedTab == index
                        val textColor = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }

                        Tab(
                            selected = selected,
                            onClick = {
                                infoScreenViewModel.selectTab(index)
                            },
                            modifier = Modifier
                                .height(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background(
                                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Text(
                                text = title,
                                color = textColor,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                if (infoScreenUIState.selectedTab == 0) {
                    UserLocationWeatherInfo(
                        locationForecastViewModel = locationForecastViewModel,
                        userLocationViewModel = userLocationViewModel,
                    )
                } else {
                    if (routeScreenUIState.pickedRouteMap == null) {


                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Empty Map Icon",
                                modifier = Modifier.size(120.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Ingen rute valgt",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Du må velge en rute for å kunne se værvarselet for din rute. \n Bruk funksjonen på hjemskjermen for å velge en rute.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }


                    } else {
                        RouteWeatherInfo(
                            routeMap = routeScreenUIState.pickedRouteMap!!,
                            locationForecastViewModel = locationForecastViewModel
                        )
                    }
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
            Text(text = "${timeLocationData.airTemperature}℃")
            WeatherIcon(
                symbolCode = timeLocationData.symbolCode,
                modifier = Modifier
                    .fillMaxSize(0.15f)
            )
            Text(
                text = String.format("%.1f", dayForecast.dayScore?.score),
                color = WeatherScore.getColor(dayForecast.dayScore?.score!!)
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = dayForecast.day,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tid",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Temperatur",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Nedbør",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Vind(kast)",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dayForecast.weatherData) { tld ->
                val nextItem = dayForecast.weatherData.zipWithNext().firstOrNull { pair ->
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
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherIcon(
                            symbolCode = tld.symbolCode,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "${tld.airTemperature}°",
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = tld.precipitationAmount.toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${tld.windSpeed}${if (tld.windSpeedOfGust != 0.0) "(${tld.windSpeedOfGust})" else ""}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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


/*
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

 */


