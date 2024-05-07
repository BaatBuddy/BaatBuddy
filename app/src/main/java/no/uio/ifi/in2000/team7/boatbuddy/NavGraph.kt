package no.uio.ifi.in2000.team7.boatbuddy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.uio.ifi.in2000.team7.boatbuddy.background_location_tracking.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.ui.BottomBar
import no.uio.ifi.in2000.team7.boatbuddy.ui.User.UserScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.setting.SettingScreen

@Composable
fun NavGraph(navController: NavHostController) {

    Scaffold(
        topBar = { topBar(navController) },
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
                    SettingScreen()
                }
                composable(route = Screen.UserScreen.route) {
                    UserScreen()
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(navController: NavHostController) {
    TopAppBar(
        title = { Text("Båt Buddy") },
        actions = {
            IconButton(onClick = { /* pop up infocard */ }) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Red)
            }
            /* IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { navController.navigate(Screen.InfoScreen.route) }) {
                Icon(Icons.Default.Info, contentDescription = "InfoSide")
            } */
            IconButton(onClick = { navController.navigate(Screen.SettingScreen.route) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = { navController.navigate(Screen.UserScreen.route) }) {
                Icon(Icons.Default.Person, contentDescription = "User")
            }
        }
    )
}





