package no.uio.ifi.in2000.team7.boatbuddy.ui.tracking

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun StopTrackingDialog(
    navController: NavController,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
) {
    Dialog(
        onDismissRequest = {
            mainViewModel.hideDialog()
        },
    ) {
        val context = LocalContext.current

        val profileUIState by profileViewModel.profileUIState.collectAsState()

        // TODO MUST BE FIXED, TOO BIG BUTTON
        // TODO navigate to new screen with the list of points, later create a route with name and ditt og datt
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Vil du avslutte turen din?")
                Button(
                    onClick = {
                        if (navController.currentDestination?.route != Screen.HomeScreen.route) {
                            navController.navigate(Screen.HomeScreen.route)
                            mainViewModel.selectScreen(0)
                        }

                        mainViewModel.hideDialog()
                        mainViewModel.stopFollowUserOnMap()

                        if (profileUIState.selectedUser != null && profileUIState.selectedBoat != null) {

                            profileViewModel.addRouteToProfile(
                                username = profileUIState.selectedUser!!.username,
                                boatname = profileUIState.selectedBoat!!.boatname,
                                route = AlertNotificationCache.points,
                                routename = ""
                            )
                            AlertNotificationCache.points = mutableListOf()
                        }

                        Intent(context, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            context.startService(this)
                        }
                    },
                    modifier = Modifier
                        .sizeIn(
                            minWidth = 32.dp,
                            minHeight = 32.dp
                        )
                        .aspectRatio(1f),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Text(
                        text = "STOP",
                        maxLines = 1,
                        fontSize = 20.sp
                    )
                }
            }
        }


    }
}