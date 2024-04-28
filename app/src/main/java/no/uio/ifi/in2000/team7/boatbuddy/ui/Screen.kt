package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object HomeScreen : Screen(route = "HomeScreen", icon = Icons.Filled.Home, label = "Hjem")
    object InfoScreen : Screen(route = "InfoScreen", icon = Icons.Filled.Info, label = "Info")
    object TrackingScreen :
        Screen(route = "TrackingScreen", icon = Icons.Filled.PlayArrow, label = "Spor")

    object RouteScreen : Screen(route = "RouteScreen", icon = Icons.Filled.List, label = "Ruter")
    object SettingScreen :
        Screen(route = "ProfileScreen", icon = Icons.Filled.Person, label = "Profil")

}