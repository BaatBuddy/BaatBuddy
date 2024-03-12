package no.uio.ifi.in2000.team7.boatbuddy.ui.metalerts


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun MetAlertsScreen (metAlertsViewModel: MetAlertsViewModel = viewModel()){

    val metAlertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()



    Column(modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth()
        .padding(35.dp)
        .verticalScroll(rememberScrollState())) {

        Text(text = metAlertsUIState.metalerts.toString(), modifier = Modifier.verticalScroll(
            rememberScrollState()))
        metAlertsUIState.metalerts?.features?.filter{it.geographicDomain == "marine"}?.forEach { feature ->
            MetCard(modifier = Modifier, feature = feature)
        }

    }


}

@Composable
fun MetCard(modifier: Modifier, feature: FeatureData) {

    Card (modifier = modifier

        .fillMaxWidth()
        .padding(16.dp),
        colors = CardDefaults.cardColors(
            when (feature.riskMatrixColor) {
                "Yellow" -> Color(0xFFFFFF00)
                "Orange" -> Color(0xFFFFA500)
                "Red" -> Color(0xFFFF0000)
                else -> Color(0xFF00FF00)
            }
        )


    ) {


        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),

        ) {

            Text(text = "start: " + feature.start)
            Text(text = "end: " + feature.end)
            if (feature.instruction.isNotEmpty()) {
                Text(text = "instruks: " + feature.instruction)
            }

        }
    }

}