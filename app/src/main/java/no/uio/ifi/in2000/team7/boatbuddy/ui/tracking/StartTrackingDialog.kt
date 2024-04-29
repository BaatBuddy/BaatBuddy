package no.uio.ifi.in2000.team7.boatbuddy.ui.tracking

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun StartTrackingDialog(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    Dialog(
        onDismissRequest = {
            mainViewModel.hideDialog()
        },
    ) {
        val context = LocalContext.current

        // TODO MUST BE FIXED, TOO BIG BUTTON
        Card {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Vil du starte sporing av turen din?")
                Button(
                    onClick = {
                        if (navController.currentDestination?.route != Screen.HomeScreen.route) {
                            navController.navigate(Screen.HomeScreen.route)
                            mainViewModel.selectScreen(0)
                        }

                        mainViewModel.hideDialog()
                        mainViewModel.startFollowUserOnMap()

                        AlertNotificationCache.points = mutableListOf()

                        Intent(context, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
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
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Text(
                        text = "START",
                        maxLines = 1,
                        fontSize = 20.sp
                    )
                }
            }
        }


    }
}