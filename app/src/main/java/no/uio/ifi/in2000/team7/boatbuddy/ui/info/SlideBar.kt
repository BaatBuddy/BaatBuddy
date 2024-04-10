package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.lang.Math.floorDiv
import kotlin.math.roundToInt


// retrieved from https://developer.android.com/develop/ui/compose/components/slider
@Composable
fun FactorSlide(
    from: Double,
    to: Double,
    unit: String
) {
    val from = from.toFloat()
    val to = to.toFloat()
    var sliderPosition by remember { mutableFloatStateOf(((from + to) / 2).roundToInt().toFloat()) }
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 4,
            valueRange = from..to
        )
        Text(text = sliderPosition.toString())
    }
}