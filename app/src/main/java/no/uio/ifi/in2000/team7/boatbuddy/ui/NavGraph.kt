package no.uio.ifi.in2000.team7.boatbuddy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.uio.ifi.in2000.team7.boatbuddy.ui.BottomBar
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.setting.SettingScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.setting.SettingViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    settingViewModel: SettingViewModel
) {

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            ) {
                composable(route = Screen.HomeScreen.route) {
                    HomeScreen()
                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen()
                }
                composable(route = Screen.SettingScreen.route) {
                    SettingScreen(settingViewModel = settingViewModel)
                }
            }
        }

    }
}




