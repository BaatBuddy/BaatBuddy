package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertLanguage
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData

@Composable
fun WeatherAlertInfoCard(
    homeViewModel: HomeViewModel,
    featureData: FeatureData,
) {
    val modifier = Modifier
        .padding(horizontal = 16.dp)

    Dialog(
        onDismissRequest = {
            homeViewModel.updateShowWeatherAlertInfoCard(null)
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // heading text

                Text(
                    text = convertLanguage(featureData.event),
                    modifier = modifier
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                IconButton(
                    onClick = {
                        homeViewModel.updateShowWeatherAlertInfoCard(null)
                    },
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = modifier
                        .size(32.dp),
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "lukk")
                }
            }

            // info text
            Text(
                text = "Beskrivelse: \n${featureData.description}\n\nKonsekvenser:\n${featureData.consequences}\n\nInstruksjoner\n${featureData.instruction}",
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp, top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}