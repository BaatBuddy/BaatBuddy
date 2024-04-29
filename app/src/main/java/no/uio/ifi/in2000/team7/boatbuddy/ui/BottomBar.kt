package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun BottomBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel
) {
    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    val profileUIState by profileViewModel.profileUIState.collectAsState()


    NavigationBar {
        listOf(
            Screen.HomeScreen,
            Screen.InfoScreen,
            Screen.TrackingScreen,
            Screen.RouteScreen,
            Screen.SettingScreen
            // add more screen if needed
        ).forEachIndexed { index, item ->
            NavigationBarItem(
                selected = mainScreenUIState.selectedScreen == index,
                onClick = {
                    if (mainScreenUIState.selectedScreen != index && item != Screen.TrackingScreen) {
                        mainViewModel.selectScreen(index)

                        navController.navigate(item.route) {

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }

                    } else if (item == Screen.TrackingScreen) {
                        if (profileUIState.selectedUser != null) {
                            if (item == Screen.TrackingScreen && !mainScreenUIState.isTrackingUser) {
                                mainViewModel.showStartDialog()
                            } else if (item == Screen.TrackingScreen) {
                                mainViewModel.showFinishDialog()
                            }
                        } else {
                            // TODO snackbar: "need to select a profile"
                        }
                    }


                },
                icon = {
                    if (mainScreenUIState.isTrackingUser && item == Screen.TrackingScreen) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = item.label) }
            )

        }


    }


}