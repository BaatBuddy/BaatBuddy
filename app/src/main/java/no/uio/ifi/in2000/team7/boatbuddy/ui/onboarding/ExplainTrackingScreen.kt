package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExplainTrackingScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            Column {
                // short explanation on how the track user button works
                // TODO image of track user

                // explanation
            }
        }
    }
}