package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.TimeWeatherData

@Composable
fun OceanRow(twd: TimeWeatherData, dayForecast: DayForecast) {
    val nextItem = dayForecast.weatherData.zipWithNext().firstOrNull { pair ->
        pair.first == twd
    }?.second
    var time = twd.time.substring(11, 13)

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
        Text(
            text = "${twd.waterTemperature}°",
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = twd.waveHeight.toString(),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Icon(
            painter = painterResource(id = R.drawable.ocean_stream_arrow),
            contentDescription = "Bølge retning",
            modifier = Modifier
                .rotate(twd.waterDirection!!.toFloat()),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}