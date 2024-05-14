package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast

@Composable
fun WeatherDropDownCard(
    dayForecast: DayForecast,
    isWeather: Boolean,
) {
    var isDroppedDown by remember { mutableStateOf(false) }
    var rotation by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                isDroppedDown = !isDroppedDown
                rotation = if (isDroppedDown) -90f
                else 0f
            }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (if (isWeather) "Værdata for " else "Vanndata for ") + dayForecast.day + if (isWeather) " ⛅" else " \uD83C\uDF0A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (dayForecast.weatherData.size > 4) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Åpne kort",
                        modifier = Modifier
                            .rotate(rotation)
                            .fillMaxSize(1f),
                        tint = MaterialTheme.colorScheme.primary
                    )

                }
            }
            if (isWeather) {
                if (isDroppedDown) {
                    WeatherTable(dayForecast = dayForecast)
                } else {
                    val middayWD = dayForecast.weatherData.filter {
                        it.time.substring(11, 13) in arrayOf("12", "13", "14", "15")
                    }
                    WeatherTable(
                        dayForecast = dayForecast.copy(
                            weatherData = if (middayWD.size == 4) middayWD
                            else dayForecast.weatherData.take(4)
                        ),
                    )
                }
            } else if (dayForecast.weatherData.all { it.waterTemperature != null }) {
                if (isDroppedDown) {
                    OceanTable(
                        dayForecast = dayForecast,
                        isShort = false,
                    )
                } else {
                    val middayWD = dayForecast.weatherData.filter {
                        it.time.substring(11, 13) in arrayOf("12", "13", "14", "15")
                    }
                    OceanTable(
                        dayForecast = dayForecast.copy(
                            weatherData = if (middayWD.size == 4) middayWD
                            else dayForecast.weatherData.take(4)
                        ),
                        isShort = true
                    )
                }
            }
        }
    }
}