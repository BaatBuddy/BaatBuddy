package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.ui.WeatherConverter.bitmapFromDrawableRes

@Composable
fun SettingScreen(settingViewModel: SettingViewModel, navController: NavController) {
    val settingUIState by settingViewModel.settingUIState.collectAsState()

    Scaffold() { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            contentAlignment = Alignment.Center
        ) {
            if (settingUIState.selectedUser == null) {
                SelectUserScreen(settingViewModel = settingViewModel, navController = navController)
            } else {
                UserProfileScreen(settingViewModel = settingViewModel)
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(settingViewModel: SettingViewModel) {

    val settingUIState by settingViewModel.settingUIState.collectAsState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profil") },
                actions = {
                    IconButton(
                        onClick = {
                            settingViewModel.unselectUser()
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
                settingUIState.selectedUser?.let {
                    UserCard(
                        user = it,
                        settingViewModel = settingViewModel
                    )
                }
                LazyColumn {
                    items(settingUIState.boatProfiles) { boatProfile ->
                        BoatCards(boatProfile = boatProfile)
                    }
                }
            }
        }
    }
}

@Composable
fun BoatCards(boatProfile: BoatProfile) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectUserScreen(settingViewModel: SettingViewModel, navController: NavController) {
    val settingUIState by settingViewModel.settingUIState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Velg profil") }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    items(settingUIState.users) {
                        UserCard(user = it, settingViewModel = settingViewModel)
                    }
                }
                Button(
                    onClick = {
                        // TODO navigate to create profile screen
                        navController.navigate("createuser")
                    }
                ) {
                    Text(text = "Lag profil")
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserProfile, settingViewModel: SettingViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                settingViewModel.selectUser(user.username)
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

@Composable
fun BoatIcon() {

}