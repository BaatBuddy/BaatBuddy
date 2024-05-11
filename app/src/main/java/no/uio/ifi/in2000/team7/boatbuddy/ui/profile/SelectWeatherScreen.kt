package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectWeatherScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()


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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (profileUIState.selectedWeather != null) {
                    WeatherSlider(
                        from = 0.0,
                        to = 30.0,
                        value = profileUIState.selectedWeather!!.airTemperature,
                        weatherType = "Luft temperatur",
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
                        weatherType = "Vind hastighet",
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
                        weatherType = "Skydekke",
                        changeValue = { cloudAreaFraction: Double ->
                            profileViewModel.updateWeatherPreference(
                                profileUIState.selectedWeather!!.copy(
                                    cloudAreaFraction = cloudAreaFraction
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
    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        value = value.toFloat(),
        onValueChange = { changeValue(it.toDouble()) },
        steps = to.toInt(),
        valueRange = from.toFloat()..to.toFloat()
    )
    Text(text = weatherType)
}