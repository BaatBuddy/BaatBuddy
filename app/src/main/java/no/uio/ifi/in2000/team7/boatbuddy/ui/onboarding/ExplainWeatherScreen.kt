package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ExplainWeatherScreen() {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // image of weather screen with explanations
                // TODO image
//                Image(
//                    painter = painterResource(id = R.drawable. TODO ),
//                    contentDescription = "Hjemmeskjerm",
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                )


                // more explanation?
                Text(
                    text = "Se været for din posisjon!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )

                Text(
                    text = "Man kan velge mellom å se været for din posisjon eller for ruten.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
            }
        }
    }
}