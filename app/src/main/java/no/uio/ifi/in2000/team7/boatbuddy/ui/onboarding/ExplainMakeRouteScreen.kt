package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team7.boatbuddy.R

@Composable
fun ExplainMakeRouteScreen(
    isDark: Boolean
) {
    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
                .background(if (isDark) MaterialTheme.colorScheme.onPrimaryContainer else Color.White)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // picture with explaination
                Image(
                    painter = painterResource(id = R.drawable.homescreen_draw_route),
                    contentDescription = "Hjemmeskjerm",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )


                // more explanation?
                Text(
                    text = "Skisser din egen rute!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                    color = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    buildAnnotatedString {
                        append("Trykk på '")

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("Tegn rute")
                        }
                        append("' knappen for å starte skisseringen.")
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "Deretter trykk punktvis på kartet som vist nedenfor.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                    color = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Image(
                    painter = painterResource(id = R.drawable.homescreen_make_route),
                    contentDescription = "Hjemmeskjerm",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )

            }
        }
    }
}