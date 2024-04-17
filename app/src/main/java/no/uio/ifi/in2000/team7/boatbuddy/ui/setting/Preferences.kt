package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Preferences() {
    TopAppBar(title = { Text(text = "Preferanse innstillinger") })
    val showCheckBoxes = remember { mutableStateOf(false) }
    val isChecked1 = remember { mutableStateOf(false) }
    val isChecked2 = remember { mutableStateOf(false) }
    FactorCheckBox(
        check = showCheckBoxes,
        factorName = "Vær faktorer"
    )

    val subBoxModifier = Modifier
        .padding(start = 16.dp)

    if (showCheckBoxes.value) {
        FactorCheckBox(
            check = isChecked1,
            factorName = "Bølgehøyde",
            modifier = subBoxModifier
        )
        FactorCheckBox(
            check = isChecked2,
            factorName = "Vanntemperatur",
            modifier = subBoxModifier
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        FactorSlide(
            preference = FactorPreference(1.0, 0.0, 5.0),
            unit = "m",
            weatherFactor = "Bølgehøyde"
        )
        FactorSlide(
            preference = FactorPreference(10.0, 0.0, 20.0),
            unit = "°C",
            weatherFactor = "Vanntemperatur"
        )
        FactorSlide(
            preference = FactorPreference(4.0, 0.0, 12.0),
            unit = "m/s",
            weatherFactor = "Vindhastighet"
        )
        FactorSlide(
            preference = FactorPreference(20.0, 0.0, 30.0),
            unit = "°C",
            weatherFactor = "Lufttemperatur"
        )
        FactorSlide(
            preference = FactorPreference(20.0, 0.0, 100.0),
            unit = "%",
            weatherFactor = "Skydekke"
        )
        FactorSlide(
            preference = FactorPreference(30.0, 0.0, 100.0),
            unit = "%",
            weatherFactor = "Relativ fuktighet"
        )
        FactorSlide(
            preference = FactorPreference(0.0, 0.0, 3.0),
            unit = "mm",
            weatherFactor = "Mengde nedbør neste 6 timer"
        )
        FactorSlide(
            preference = FactorPreference(0.0, 0.0, 3.0),
            unit = "mm",
            weatherFactor = "FILLER"
        )
    }
}