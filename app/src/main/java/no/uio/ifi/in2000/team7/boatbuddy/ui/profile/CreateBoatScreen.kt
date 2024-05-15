package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBoatScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Lag båt") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            CreateBoatSegment(
                profileViewModel = profileViewModel,
                navController = navController,
            )

        }
    }
}


fun checkInputsBoatProfile(
    createUserUIState: CreateUserUIState,
    profileUIState: ProfileUIState,
    invalidMap: MutableMap<String, Boolean>,
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val username = profileUIState.selectedUser?.username ?: ""
    val boatname = createUserUIState.boatname
    val boatSpeed = createUserUIState.boatSpeed
    val safetyDepth = createUserUIState.safetyDepth
    val safetyHeight = createUserUIState.safetyHeight

    invalidMap["username"] = username.isBlank()
    invalidMap["boatname"] = boatname.isBlank()
    invalidMap["boatSpeed"] = boatSpeed.isBlank()
    invalidMap["safetyDepth"] = safetyDepth.isBlank()
    invalidMap["safetyHeight"] = safetyHeight.isBlank()

    if (invalidMap.all { !it.value }) {
        profileViewModel.addBoat(
            username = username,
            boatname = boatname,
            boatSpeed = boatSpeed,
            safetyDepth = safetyDepth,
            safetyHeight = safetyHeight,
        )
        profileViewModel.getAllBoatsUsername()
        profileViewModel.clearCreateProfile()
        navController.popBackStack()
    }
}