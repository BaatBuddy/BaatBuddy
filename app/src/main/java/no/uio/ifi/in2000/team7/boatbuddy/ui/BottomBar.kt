package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.primaryContainerLight
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.primaryLight
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.secondaryContainerLight


@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
    ) {
        listOf(
            Screen.HomeScreen,
            Screen.InfoScreen
            // add more screen if needed
        ).forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index

                        navController.navigate(item.route) {

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            
                            launchSingleTop = true
                            restoreState = true
                        }

                    }

                },
                icon = { Icon(imageVector = item.icon, contentDescription = null) })

        }


    }


}