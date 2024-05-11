package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectWeatherScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    mainViewModel: MainViewModel,
) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()
    mainViewModel.selectScreen(4)

    // sliders with weatherpreferences
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Velg vær preferanse"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (profileUIState.selectedWeather != null) {
                    WeatherSlider(
                        from = 10.0,
                        to = 30.0,
                        value = profileUIState.selectedWeather!!.airTemperature,
                        weatherType = "Luft temperatur: ${profileUIState.selectedWeather!!.airTemperature}℃",
                        changeValue = { airTemperature: Double ->
                            profileViewModel.updateWeatherPreference(
                                profileUIState.selectedWeather!!.copy(
                                    airTemperature = airTemperature
                                )
                            )
                        }
                    )
                    WeatherSlider(
                        from = 0.0,
                        to = 12.0,
                        value = profileUIState.selectedWeather!!.windSpeed,
                        weatherType = "Vind hastighet: ${profileUIState.selectedWeather!!.windSpeed}m/s",
                        changeValue = { windSpeed: Double ->
                            profileViewModel.updateWeatherPreference(
                                profileUIState.selectedWeather!!.copy(
                                    windSpeed = windSpeed
                                )
                            )
                        }
                    )
                    WeatherSlider(
                        from = 0.0,
                        to = 100.0,
                        value = profileUIState.selectedWeather!!.cloudAreaFraction,
                        weatherType = "Skydekke: ${profileUIState.selectedWeather!!.cloudAreaFraction}%",
                        changeValue = { cloudAreaFraction: Double ->
                            profileViewModel.updateWeatherPreference(
                                profileUIState.selectedWeather!!.copy(
                                    cloudAreaFraction = cloudAreaFraction
                                )
                            )
                        }
                    )
                    OptionalWeatherSlider(
                        from = 10.0,
                        to = 25.0,
                        value = profileUIState.selectedWeather!!.waterTemperature,
                        weatherType = "Vann temperatur: ${profileUIState.selectedWeather!!.waterTemperature}℃",
                        changeValue = { waterTemperature: Double ->
                            profileViewModel.updateWeatherPreference(
                                profileUIState.selectedWeather!!.copy(
                                    waterTemperature = waterTemperature
                                )
                            )
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun WeatherSlider(
    from: Double,
    to: Double,
    value: Double,
    weatherType: String,
    changeValue: (Double) -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = weatherType)
        Slider(
            modifier = modifier,
            value = value.toFloat(),
            onValueChange = { changeValue(it.roundToInt().toDouble()) },
            steps = to.toInt() - from.toInt() - 1,
            valueRange = from.toFloat()..to.toFloat()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier,
        ) {
            Text(text = from.toString())
            Text(text = to.toString())
        }

    }
}

@Composable
fun OptionalWeatherSlider(
    from: Double,
    to: Double,
    value: Double?,
    weatherType: String,
    changeValue: (Double) -> Unit,
) {
    var enabled by remember { mutableStateOf(value != null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checkbox(
            checked = enabled,
            onCheckedChange = {
                enabled = it
                if (enabled) {
                    changeValue(15.0)
                }
            },
        )

        if (enabled && value != null) {
            WeatherSlider(
                from = from,
                to = to,
                value = value,
                weatherType = weatherType,
                changeValue = changeValue,
            )
        }
    }

}