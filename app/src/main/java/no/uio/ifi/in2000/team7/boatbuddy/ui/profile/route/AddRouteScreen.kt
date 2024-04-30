package no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddRouteScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            // text field to name
            // text to display times
            // box with route so far
            // continue button
            // finish button
        }

    }
}