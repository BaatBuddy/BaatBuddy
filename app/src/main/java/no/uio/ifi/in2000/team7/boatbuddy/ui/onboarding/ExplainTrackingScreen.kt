package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R

@Composable
fun ExplainTrackingScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // short explanation on how the track user button works
                // TODO image of track user
                Image(
                    painter = painterResource(id = R.drawable.tracking_button),
                    contentDescription = "Hjemmeskjerm",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )

                Text(
                    text = "Spor dine ruter!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )

                Text(
                    text = "Starter sporing av ruten i tillegg til å varsle dersom man befinner seg i fareområder.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )

                Text(
                    text = "Trykk på samme knapp for å avslutte sporing og lagre ruten.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
                // explanation

                Image(
                    painter = painterResource(id = R.drawable.track_dialog),
                    contentDescription = "Hjemmeskjerm",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}