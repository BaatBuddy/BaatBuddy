package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
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
import no.uio.ifi.in2000.team7.boatbuddy.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfile

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()

    Scaffold() { paddingValue ->
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
fun BoatCards(boatProfile: BoatProfile, profileViewModel: ProfileViewModel) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    ElevatedCard(
        onClick = {
            profileUIState.selectedUser?.username?.let {
                profileViewModel.selectBoat(
                    boatname = boatProfile.boatname,
                    username = it
                )
            }
        },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (boatProfile.isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Text(text = "Båtnavn: " + boatProfile.boatname)
                Text(text = "Båt hastighet: " + boatProfile.boatSpeed)
                Text(text = "Sikkerhets dybde: " + boatProfile.safetyDepth)
                Text(text = "Sikkerhets høyde: " + boatProfile.safetyHeight)
            }
        }
    }
}

@Composable
fun UserCard(user: UserProfile, profileViewModel: ProfileViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                profileViewModel.selectUser(user.username)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = user.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.W900,
            )
            Text(
                text = user.username,
                fontSize = 12.sp,
                fontWeight = FontWeight.W100,
            )
        }
    }
}