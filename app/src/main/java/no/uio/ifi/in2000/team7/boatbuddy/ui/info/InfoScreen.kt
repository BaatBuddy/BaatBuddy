package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.NetworkConnectivityViewModel
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertWeatherResId
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.NetworkConnectivityObserver
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
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
    mapboxViewModel: MapboxViewModel,
    navController: NavController,
    networkConnectivityViewModel: NetworkConnectivityViewModel
) {

    val infoScreenUIState by infoScreenViewModel.infoScreenUIState.collectAsState()
    val routeScreenUIState by profileViewModel.routeScreenUIState.collectAsState()
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val userLocationUIState by userLocationViewModel.userLocationUIState.collectAsState()
    val status by networkConnectivityViewModel.connectionUIState.collectAsState()

    if (profileUIState.updateWeather) {
        locationForecastViewModel.updateScore()
        profileViewModel.stopUpdateWeather()
    }

    mainViewModel.selectScreen(1)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Været")
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
                    indicator = {}
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
                    if (userLocationUIState != null) {
                        if (status != NetworkConnectivityObserver.Status.Available) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(120.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_wifi_off_24),
                                    contentDescription = "WiFi Icon",
                                    modifier = Modifier.size(120.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Du er ikke koblet til Internett",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Du må koble til Internett for å kunne se værforholdene i ditt område.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        } else {
                            UserLocationWeatherInfo(
                                locationForecastViewModel = locationForecastViewModel,
                                userLocationViewModel = userLocationViewModel,
                            )
                        }
                    }


                } else {
                    if (routeScreenUIState.pickedRouteMap == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Empty Map Icon",
                                modifier = Modifier.size(120.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
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
                            locationForecastViewModel = locationForecastViewModel,
                            profileViewModel = profileViewModel,
                            mapboxViewModel = mapboxViewModel,
                            navController = navController,
                            mainViewModel = mainViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    dayForecast: DayForecast,
    selectedDay: DayForecast?,
    changeDay: () -> Unit,

    ) {
    val emojiString = scoreToEmoji(dayForecast.dayScore?.score) ?: ""
    val timeLocationData = dayForecast.middayWeatherData
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable { run(changeDay) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            if (selectedDay == dayForecast) MaterialTheme.colorScheme.surfaceContainer
            else MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = if (selectedDay == dayForecast) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.onPrimaryContainer
        ) else null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                text = dayForecast.day,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${timeLocationData.airTemperature}℃",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            WeatherIcon(
                symbolCode = timeLocationData.symbolCode,
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = emojiString,
                fontSize = 30.sp

            )
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
                onClick = onDismissRequested
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


