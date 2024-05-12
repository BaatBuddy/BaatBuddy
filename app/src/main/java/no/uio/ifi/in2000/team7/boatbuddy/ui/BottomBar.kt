package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

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
            Screen.SettingsScreen
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
                                profileViewModel.updateCurrentRoute()
                                mainViewModel.showFinishDialog()
                            }
                        } else {
                            // TODO snackbar: "need to select a profile"
                            mainViewModel.showNoUserDialog()
                        }
                    }


                },
                icon = {
                    if (mainScreenUIState.isTrackingUser && item == Screen.TrackingScreen) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    } else if (item.icon is ImageVector) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = loadDrawableAsImageVector(resourceId = item.icon as Int),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    }
                },
                label = {Text(text = item.label)}
            )

        }


    }


}