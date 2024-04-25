package no.uio.ifi.in2000.team7.boatbuddy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object HomeScreen : Screen(route = "HomeScreen", icon = Icons.Filled.Home, label = "Hjem")
    object InfoScreen : Screen(route = "InfoScreen", icon = Icons.Filled.Info, label = "Info")
    object SettingScreen :
        Screen(route = "SettingScreen", icon = Icons.Filled.Settings, label = "Innstillinger")

}