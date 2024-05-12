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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import no.uio.ifi.in2000.team7.boatbuddy.data.location.UpdateDataWorker
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
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        val context = LocalContext.current

        Card(
            modifier = Modifier
                .widthIn(min = 300.dp) // Set a minimum width for the card
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vil du starte sporing av turen din?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            mainViewModel.hideDialog()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Avbryt",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
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
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Start",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}