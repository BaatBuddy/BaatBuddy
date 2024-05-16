package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding.ExplainMakeRouteScreen

// popup card for instructions
@Composable
fun ExplanationCard(
    homeViewModel: HomeViewModel,
) {
    val modifier = Modifier
        .padding(horizontal = 16.dp)

    Dialog(
        onDismissRequest = {
            homeViewModel.updateShowExplanationCard(false)
        }
    ) {


        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = Modifier
                .padding(vertical = 100.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rute tegning",
                    modifier = modifier
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                IconButton(
                    onClick = {
                        homeViewModel.updateShowExplanationCard(false)
                    },
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = modifier
                        .size(32.dp),
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
            }
            ExplainMakeRouteScreen(isDark = true)

            /*
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.homescreen_make_route),
                    contentDescription = "bilde av oslo med skissert rute",
                    modifier = modifier
                        .clip(RoundedCornerShape(8.dp)),
                )

                // heading text


                // info text
                Text(
                    text = "Her kan man trykke på tegn rute knappen helt nederst i høyre hjørne" +
                            " for å starte å tegne en rute på kartet. Deretter trykker man inn minst" +
                            " 2 og opp til 10 punkter for å lage en skisse av ruten. \n\nNå kan man trykke" +
                            " på generer rute knappen for å starte genereringen av en mer raffinert rute." +
                            " \n\nVæret for ruten vil automatisk dukke opp på bunnen, men frykt ikke for å" +
                            " fjerne den fordi man kan få informasjonen opp igjen med en 'vis vær' knapp.",
                    modifier = modifier
                        .padding(bottom = 16.dp, top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }*/


        }
    }
}