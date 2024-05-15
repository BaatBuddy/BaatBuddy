package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FinishOnBoardingScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            Column {
                // Try to make your first route!
                Text(text = "Prøv å lag din først rute!")
            }
        }
    }
}