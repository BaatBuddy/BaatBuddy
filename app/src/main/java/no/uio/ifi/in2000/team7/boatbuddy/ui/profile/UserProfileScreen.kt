package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {

    val profileUIState by profileViewModel.profileUIState.collectAsState()
    profileViewModel.getAllBoatsUsername()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = "Profil") },
                actions = {
                    IconButton(
                        onClick = {
                            profileViewModel.unselectUser()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
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
            Column {
                Text(text = "Profil")
                profileUIState.selectedUser?.let {
                    UserCard(
                        user = it,
                        profileViewModel = profileViewModel
                    )
                }
                Text(text = "Båter")
                LazyColumn {
                    items(profileUIState.boats) { boatProfile ->
                        BoatCards(boatProfile = boatProfile, profileViewModel = profileViewModel)
                    }
                }
                Button(
                    onClick = {
                        navController.navigate("createboat")
                    }
                ) {
                    Text(text = "Lag ny båt")
                }
            }
        }
    }
}