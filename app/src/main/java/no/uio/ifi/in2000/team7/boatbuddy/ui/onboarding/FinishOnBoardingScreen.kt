package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R

@Composable
fun FinishOnBoardingScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState(700)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_done_route),
                    contentDescription = "map lines",
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Try to make your first route!
                    Image(
                        painter = painterResource(id = R.drawable.onboarding_arrow),
                        contentDescription = "map lines",
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFf5fafd))
                    ) {
                        Text(
                            text = "Lag din første rute!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(20.dp)
                        )
                    }

                }
                Image(
                    painter = painterResource(id = R.drawable.onboarding_done_weather),
                    contentDescription = "map lines",
                    modifier = Modifier
                        .padding(16.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = MaterialTheme.colorScheme.primary
                        )
                        .clip(RoundedCornerShape(16.dp))


                )
            }
        }
    }
}