package no.uio.ifi.in2000.team7.boatbuddy.ui.route

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
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
        // TODO MUST BE FIXED, TOO BIG BUTTON
        // TODO navigate to new screen with the list of points, later create a route with name and ditt og datt
        Card(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vil du avslutte turen din?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                )

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                            mainViewModel.hideBottomBar()
                            navController.navigate("addroute")

                            mainViewModel.hideDialog()

                            profileViewModel.updateCurrentRouteTime()
                            profileViewModel.updateCurrentRoute()
                        },
                        modifier = Modifier
                            .size(100.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)
                        )
                        Text(
                            text = "FERDIG",
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                    }

                }
            }
        }


    }
}