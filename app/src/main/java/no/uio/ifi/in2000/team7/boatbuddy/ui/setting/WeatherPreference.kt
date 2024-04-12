package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences

@Composable
fun WeatherPreferences() {
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
    }
}