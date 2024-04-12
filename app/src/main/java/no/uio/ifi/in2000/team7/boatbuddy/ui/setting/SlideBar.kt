package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.FactorPreference
import kotlin.math.roundToInt


// retrieved from https://developer.android.com/develop/ui/compose/components/slider
@Composable
fun FactorSlide(
    preference: FactorPreference,
    unit: String,
    weatherFactor: String
) {
    val from = preference.from.toFloat()
    val to = (preference.to + if (unit != "%") 1 else 0).toFloat()

    var sliderPosition by remember {
        mutableFloatStateOf(
            preference.value.toFloat()
        )
    }


    Column(
        modifier = Modifier
            .fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = weatherFactor)
        Text(
            text = "${
                if (sliderPosition == to && unit != "%") (to.toInt() - 1).toString() + "+" else sliderPosition.roundToInt()
            } $unit"
        )

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = to.toInt() - 1,
            valueRange = from..to
        )
    }
}