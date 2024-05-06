package no.uio.ifi.in2000.team7.boatbuddy.ui.route

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.data.foreground_location.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRouteScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    mainViewModel: MainViewModel,
    mapboxViewModel: MapboxViewModel,
) {

    var loading by remember { mutableStateOf(true) }
    var failed by remember { mutableStateOf(true) }

    val routeScreenUIState by profileViewModel.routeScreenUIState.collectAsState()
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    profileViewModel.updateCurrentRouteTime()

    val context = LocalContext.current

    BackHandler {
        mainViewModel.showBottomBar()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            mainViewModel.showBottomBar()
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            mainViewModel.showBottomBar()
                            mainViewModel.stopFollowUserOnMap()
                            mainViewModel.stopTrackingUser()
                            navController.popBackStack()
                        }
                    ) {
                        Text(text = "FORKAST")
                    }
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // text field to name
                TextField(
                    value = routeScreenUIState.routeName,
                    onValueChange = { profileViewModel.updateRouteName(it) },
                    label = {
                        Text(
                            text = "Navn på turen"
                        )
                    }
                )

                TextField(
                    value = routeScreenUIState.routeDescription,
                    onValueChange = { profileViewModel.updateRouteDescription(it) },
                    label = {
                        Text(
                            text = "Beskrivelse av turen"
                        )
                    }
                )

                // text to display times
                Text(text = "Tid: ${routeScreenUIState.startTime} - ${routeScreenUIState.finishTime}")

                // box with route so far
                routeScreenUIState.currentRouteView?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Map with route",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onSuccess = {
                            loading = false
                            failed = false
                        },
                        onError = {
                            loading = false
                            failed = true // couldnt make a map, therefor didnt move
                        }
                    )
                }
                if (loading) {
                    CircularProgressIndicator()
                }
                if (failed) {
                    Text(text = "Turen er ikke lang nok!")
                }



                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // continue button
                    Button(
                        onClick = {
                            navController.popBackStack()
                            mainViewModel.showBottomBar()
                            mainViewModel.startFollowUserOnMap()
                        },
                        modifier = Modifier
                            .size(120.dp)
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
                            text = "FORTSETT",
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                    }
                    // finish button
                    Button(
                        onClick = {
                            // check if route is long enough

                            Intent(context, LocationService::class.java).apply {
                                action = LocationService.ACTION_STOP
                                context.startService(this)
                            }

                            profileViewModel.addRouteToProfile(
                                username = profileUIState.selectedUser!!.username,
                                boatname = profileUIState.selectedBoat!!.boatname,
                                routename = routeScreenUIState.routeName,
                                routeDescription = routeScreenUIState.routeDescription,
                                route = mapboxUIState.routePath
                            )
                            mainViewModel.showBottomBar()
                            navController.popBackStack()
                            mainViewModel.stopTrackingUser()
                        },
                        modifier = Modifier
                            .size(120.dp)
                            .aspectRatio(1f),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        enabled = !failed
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)
                        )
                        Text(
                            text = "LAGRE",
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                    }

                }
            }
        }
    }
}