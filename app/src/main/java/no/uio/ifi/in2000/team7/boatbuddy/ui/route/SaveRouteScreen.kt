import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRouteScreen(
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

    val context = LocalContext.current

    BackHandler {
        mainViewModel.showBottomBar()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lagre generert rute",
                        style = MaterialTheme.typography.titleLarge
                    )
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
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = routeScreenUIState.routeName,
                onValueChange = { profileViewModel.updateRouteName(it) },
                label = {
                    Text(
                        text = "Navn på turen",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = routeScreenUIState.routeDescription,
                onValueChange = { profileViewModel.updateRouteDescription(it) },
                label = {
                    Text(
                        text = "Beskrivelse av turen",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    routeScreenUIState.currentRouteView?.let {
                        AsyncImage(
                            model = it,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Map with route",
                            modifier = Modifier.fillMaxSize(),
                            onSuccess = {
                                loading = false
                                failed = false
                            },
                            onError = {
                                loading = false
                                failed = true
                            }
                        )
                    }

                    if (loading) {
                        CircularProgressIndicator()
                    }
                    if (failed) {
                        Text(
                            text = "Klarte ikke å hente rute",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                        mainViewModel.showBottomBar()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(48.dp),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Exit",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Exit",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        Intent(context, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            context.startService(this)
                        }
                        profileViewModel.addRouteToProfile(
                            username = profileUIState.selectedUser!!.username,
                            boatname = profileUIState.selectedBoat!!.boatname,
                            routename = routeScreenUIState.routeName,
                            routeDescription = routeScreenUIState.routeDescription,
                            route = mapboxUIState.generatedRoute?.route?.route
                        )

                        mainViewModel.showBottomBar()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    enabled = !failed,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LAGRE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}