package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter

// helping composable for alert icon
@Composable
fun AlertIcon(event: String, riskMatrixColor: String, modifier: Modifier) {
    Icon(
        painter = painterResource(
            id = WeatherConverter.convertAlertResId(
                event = event,
                riskMatrixColor = riskMatrixColor,
                context = LocalContext.current
            )
        ),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
    )
}