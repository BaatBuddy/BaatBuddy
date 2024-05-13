package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData


@Composable
fun WeatherRow(tld: TimeWeatherData, dayForecast: DayForecast) {
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
                else -> time
            }
        } else {
            time
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
