package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
                        text = "Værpreferanser"
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
                },
                actions = {
                    Button(
                        onClick = {
                            if (profileUIState.selectedWeather != null) {
                                profileViewModel.replaceWeatherPreference(profileUIState.selectedWeather!!)

                            }
                            navController.popBackStack()
                        }
                    ) {
                        Text(text = "Lagre")
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (profileUIState.selectedWeather != null) {
                        Text(
                            text = "Standard preferanser",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        ElevatedCard(
                            modifier = Modifier
                                .padding(16.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = MaterialTheme.colorScheme.primary
                                )
                                .clip(RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                StandardWeatherSlider(
                                    from = 10.0,
                                    to = 30.0,
                                    value = profileUIState.selectedWeather!!.airTemperature,
                                    weatherType = "Luft temperatur: ${profileUIState.selectedWeather!!.airTemperature}",
                                    unit = "℃",
                                    changeValue = { airTemperature: Double ->
                                        profileViewModel.updateWeatherPreference(
                                            profileUIState.selectedWeather!!.copy(
                                                airTemperature = airTemperature
                                            )
                                        )
                                    }
                                )

                                StandardWeatherSlider(
                                    from = 0.0,
                                    to = 12.0,
                                    value = profileUIState.selectedWeather!!.windSpeed,
                                    weatherType = "Vind hastighet: ${profileUIState.selectedWeather!!.windSpeed}",
                                    unit = "m/s",
                                    changeValue = { windSpeed: Double ->
                                        profileViewModel.updateWeatherPreference(
                                            profileUIState.selectedWeather!!.copy(
                                                windSpeed = windSpeed
                                            )
                                        )
                                    }
                                )
                                StandardWeatherSlider(
                                    from = 0.0,
                                    to = 100.0,
                                    value = profileUIState.selectedWeather!!.cloudAreaFraction,
                                    weatherType = "Skydekke: ${profileUIState.selectedWeather!!.cloudAreaFraction}",
                                    unit = "%",
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

                        Text(
                            text = "Valgfrie preferanser",
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        ElevatedCard(
                            modifier = Modifier
                                .padding(16.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = MaterialTheme.colorScheme.primary
                                )
                                .clip(RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OptionalWeatherSlider(
                                    from = 10.0,
                                    to = 25.0,
                                    value = profileUIState.selectedWeather!!.waterTemperature,
                                    weatherType = "Vann temperatur",
                                    unit = "℃",
                                    changeValue = { waterTemperature: Double? ->
                                        profileViewModel.updateWeatherPreference(
                                            profileUIState.selectedWeather!!.copy(
                                                waterTemperature = waterTemperature
                                            )
                                        )
                                    }
                                )

                                OptionalWeatherSlider(
                                    from = 0.0,
                                    to = 100.0,
                                    value = profileUIState.selectedWeather!!.relativeHumidity,
                                    weatherType = "Relativ fuktighet",
                                    unit = "%",
                                    changeValue = { relativeHumidity: Double? ->
                                        profileViewModel.updateWeatherPreference(
                                            profileUIState.selectedWeather!!.copy(
                                                relativeHumidity = relativeHumidity
                                            )
                                        )
                                    }
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(150.dp)
                        )
                    }
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
    changeValue: (Double) -> Unit,
    unit: String,
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
            Text(text = from.toString() + unit)
            Text(text = to.toString() + unit)
        }

    }
}


@Composable
fun StandardWeatherSlider(
    from: Double,
    to: Double,
    value: Double,
    weatherType: String,
    unit: String,
    changeValue: (Double) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = weatherType + unit)
        WeatherSlider(
            from = from,
            to = to,
            value = value,
            changeValue = changeValue,
            unit = unit,
        )
    }
}

@Composable
fun OptionalWeatherSlider(
    from: Double,
    to: Double,
    value: Double?,
    weatherType: String,
    unit: String,
    changeValue: (Double?) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = value != null,
                onCheckedChange = {
                    if (it) {
                        changeValue(15.0)
                    } else {
                        changeValue(null)
                    }
                },
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.primary)
            )
            Text(text = if (value != null) "$weatherType: $value $unit" else "Inkluder " + weatherType.lowercase())

        }

        AnimatedVisibility(visible = value != null) {
            if (value != null) {
                WeatherSlider(
                    from = from,
                    to = to,
                    value = value,
                    changeValue = changeValue,
                    unit = unit,
                )
            }
        }

    }

}

