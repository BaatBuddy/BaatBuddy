package no.uio.ifi.in2000.team7.boatbuddy.ui.route

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache
import no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.UpdateDataWorker
import java.util.concurrent.TimeUnit

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
                Text(
                    text = "Vil du starte sporing av turen din?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            mainViewModel.hideDialog()
                        },
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1f),

                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)
                        )
                        Text(
                            text = "AVBRYT",
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                    }
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

                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()


                            val uploadWorkRequest: PeriodicWorkRequest =
                                PeriodicWorkRequest.Builder(
                                    UpdateDataWorker::class.java,
                                    15,
                                    TimeUnit.MINUTES
                                )
                                    .setConstraints(constraints)
                                    .build()

                            val workManager = WorkManager.getInstance(context)
                            workManager
                                .enqueueUniquePeriodicWork(
                                    "test",
                                    ExistingPeriodicWorkPolicy.KEEP,
                                    uploadWorkRequest,
                                )
                        },
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1f),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)
                        )
                        Text(
                            text = "START",
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                    }

                }
            }
        }


    }
}