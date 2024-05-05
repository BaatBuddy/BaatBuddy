package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.WeekForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastUIState
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.WeatherIcon

@Composable
fun SwipeUpContent(
    locationForecastUIState: LocationForecastUIState
) {

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

        locationForecastUIState.weekdayForecast?.let { DayWeatherTable(weekForecast = it) }
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












