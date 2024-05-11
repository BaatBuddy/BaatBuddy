package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import no.uio.ifi.in2000.team7.boatbuddy.R



sealed class Screen(val route: String, val icon: Any, val label: String) {

    object HomeScreen :
        Screen(route = "homescreen", icon = R.drawable.baseline_sailing_24, label = "Reise")

    object InfoScreen : Screen(route = "infoscreen", icon = Icons.Filled.Info, label = "Info")
    object TrackingScreen :
        Screen(route = "trackingscreen", icon = Icons.Filled.PlayArrow, label = "Spor")

    object RouteScreen : Screen(route = "routescreen", icon = Icons.Filled.List, label = "Ruter")
    object SettingsScreen :
        Screen(route = "profilescreen", icon = Icons.Filled.Person, label = "Profil")

}


@Composable
fun loadDrawableAsImageVector(resourceId: Int): ImageVector {
    return ImageVector.vectorResource(id = resourceId)
}