package no.uio.ifi.in2000.team7.boatbuddy.ui.route

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.APIStatus
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.main.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun SaveRouteButton(
    mainViewModel: MainViewModel,
    navController: NavController,
    modifier: Modifier,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    colors: ButtonColors,
) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    Button(
        onClick = {
            if (profileUIState.selectedUser != null && profileUIState.selectedBoat != null && mapboxUIState.routeData is APIStatus.Success) {
                profileViewModel.updateCurrentRoute(mapboxUIState.generatedRoute?.route?.route)
                navController.navigate("saveroute")
                mainViewModel.hideBottomBar()
            } else {
                mainViewModel.showNoUserDialog()
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = colors,
    ) {
        Text(
            text = "Lagre rute",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}