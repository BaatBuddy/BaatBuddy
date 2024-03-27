package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.Screen
import no.uio.ifi.in2000.team7.boatbuddy.data.UserLocation

@Composable
fun BottomBar(
    navController: NavController
) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        listOf(
            Screen.HomeScreen,
            Screen.InfoScreen,
            Screen.UserLocationScreen
        ).forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        if (item == Screen.UserLocationScreen) {
                            //locationPermissionLauncher.launch(permissionsList)
                            UserLocation.checkPermissions()
                        } else {
                            navController.navigate(item.route)
                        }

                    }

                },
                icon = { Icon(imageVector = item.icon, contentDescription = null) })

        }


    }


}