package no.uio.ifi.in2000.team7.boatbuddy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val icon: ImageVector) {
    object HomeScreen : Screen(route = "HomeScreen", icon = Icons.Filled.Home)
    object InfoScreen : Screen(route = "InfoScreen", icon = Icons.Filled.Info)

}