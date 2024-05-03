package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Båt Buddy") },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { navController.navigate(Screen.InfoScreen.route) }) {
                Icon(Icons.Default.Info, contentDescription = "Info")
            }
            IconButton(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}

