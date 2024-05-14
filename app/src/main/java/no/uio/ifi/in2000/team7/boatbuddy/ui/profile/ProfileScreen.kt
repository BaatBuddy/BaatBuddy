package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import no.uio.ifi.in2000.team7.boatbuddy.data.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    mainViewModel.selectScreen(4)
    val profileUIState by profileViewModel.profileUIState.collectAsState()

    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            contentAlignment = Alignment.Center
        ) {
            if (profileUIState.selectedUser == null) {
                SelectUserScreen(
                    profileViewModel = profileViewModel,
                    navController = navController
                )
            } else {
                UserProfileScreen(
                    profileViewModel = profileViewModel,
                    navController = navController
                )
            }

        }

    }
}

@Composable
fun BoatCards(
    boatProfile: BoatProfile,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()

    ElevatedCard(
        onClick = {
            if (!profileUIState.isSelectingBoat) {
                navController.navigate("selectboat")
                profileViewModel.startSelectingBoats()
            } else {
                profileUIState.selectedUser?.username?.let {
                    profileViewModel.selectBoat(
                        boatname = boatProfile.boatname,
                        username = it
                    )
                }
                profileViewModel.stopSelectingBoats()
                navController.popBackStack()
            }
        },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (boatProfile.isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = boatProfile.boatname,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500
                )
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Hastighet: ")
                        Text(text = boatProfile.boatSpeed)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Dybde: ")
                        Text(text = boatProfile.safetyDepth)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Høyde: ")
                        Text(text = boatProfile.safetyHeight)
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserProfile, profileViewModel: ProfileViewModel) {
    val profileUiState by profileViewModel.profileUIState.collectAsState()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                profileViewModel.selectUser(user.username)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = user.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@${user.username}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            if(profileUiState.selectedUser == null){
                Icon(

                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Profile",
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(32.dp)
                )
            }


        }
    }
}

@Composable
fun WeatherPreferencesCard(
    weatherPreferences: WeatherPreferences,
    navController: NavController
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("selectweather")
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "Luft temperatur:")
            Text(text = weatherPreferences.airTemperature.toString() + "℃")
            Text(text = "Vind hastighet:")
            Text(text = weatherPreferences.windSpeed.toString() + "m/s")
            Text(text = "Prosent skyer:")
            Text(text = weatherPreferences.cloudAreaFraction.toString() + "%")
            if (weatherPreferences.waterTemperature != null) {
                Text(text = "Vann temperatur:")
                Text(text = weatherPreferences.waterTemperature.toString() + "℃")
            }
            if (weatherPreferences.relativeHumidity != null) {
                Text(text = "Relativ fuktighet:")
                Text(text = weatherPreferences.relativeHumidity.toString() + "%")
            }
        }
    }
}