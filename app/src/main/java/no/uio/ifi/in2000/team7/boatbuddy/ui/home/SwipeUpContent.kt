package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.WeatherIcon
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun SwipeUpContent(
    weekdayForecastRoute: WeekForecast?,
    profileViewModel: ProfileViewModel,
    mainViewModel: MainViewModel,
    mapboxViewModel: MapboxViewModel,
) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = "Din reise",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )
        // weather alerts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            //alerts
            //TODO make weather alert thing
            VerticalDivider(
                thickness = 2.dp,
                modifier = Modifier
                    .padding(8.dp)
            )
            // save route
            // TODO check if selected user
            Button(onClick = {
                if (profileUIState.selectedUser != null && profileUIState.selectedBoat != null && mapboxUIState.routeData is APIStatus.Success) {
                    Log.i("ASDASD", mapboxUIState.routePath.toString())
                    profileViewModel.updateCurrentRoute(mapboxUIState.routePath)
                    mainViewModel.showFinishDialog()
                }
            }) {

            }
        }

        if (weekdayForecastRoute != null) {
            DayWeatherTable(weekForecast = weekdayForecastRoute)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun DayWeatherTable(weekForecast: WeekForecast) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Dag")
            Text(text = "Temperatur")
            Text(text = "Score")
            Text(text = "")
        }
        weekForecast.days.toList().map { it.second }.sortedBy { it.date }.forEach { dayForecast ->
            DayWeatherRow(dayForecast = dayForecast)
        }
    }
}

@Composable
fun DayWeatherRow(dayForecast: DayForecast) {
    val middayWeatherData = dayForecast.middayWeatherData
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
    ) {
        Text(text = dayForecast.day)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherIcon(
                symbolCode = middayWeatherData.symbolCode,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(text = "${middayWeatherData.airTemperature}℃")
        }
        Text(text = "%.1f".format(dayForecast.dayScore?.score))
        Text(text = "") // TODO add a clickable element that takes user to a new screen with more detailed info (bring dayForecast data to the next screen since it contains weather for the whole day)

    }
}












