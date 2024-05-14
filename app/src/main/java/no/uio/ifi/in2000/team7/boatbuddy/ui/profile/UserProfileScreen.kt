package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()
    profileViewModel.getAllBoatsUsername()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profil") },
                actions = {
                    IconButton(

                        onClick = {
                            // TODO prevent this if the user is currently tracking a route
                            profileViewModel.unselectUser()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "",
                        )
                    }
                    IconButton(
                        onClick = {
                            // TODO edit user
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = ""
                        )
                    }

                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Valgt profil",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W900
                )
                profileUIState.selectedUser?.let {
                    UserCard(
                        user = it,
                        profileViewModel = profileViewModel
                    )
                }
                Text(
                    text = "Valgt båt",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W900
                )

                profileUIState.selectedBoat?.let {
                    BoatCards(
                        boatProfile = it,
                        profileViewModel = profileViewModel,
                        navController = navController,
                    )
                }
                Button(
                    onClick = {

                        if (!profileUIState.isSelectingBoat) {
                            navController.navigate("selectboat")
                            profileViewModel.startSelectingBoats()
                        } else {
                            profileUIState.selectedUser?.username?.let {
                                profileViewModel.selectBoat(
                                    boatname = profileUIState.selectedBoat!!.boatname,
                                    username =  profileUIState.selectedBoat!!.username
                                )
                            }
                            profileViewModel.stopSelectingBoats()
                            navController.popBackStack()
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Liste over båter")
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.AutoMirrored.Outlined.List, contentDescription = "List icon")


                }


                Text(
                    text = "Vær preferanser",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W900
                )
                profileUIState.selectedUser?.let {
                    WeatherPreferencesCard(
                        weatherPreferences = it.preferences,
                        navController = navController,
                    )
                }

            }
        }
    }
}