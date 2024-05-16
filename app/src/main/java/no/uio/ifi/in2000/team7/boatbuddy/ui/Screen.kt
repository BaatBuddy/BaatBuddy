package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import no.uio.ifi.in2000.team7.boatbuddy.R


sealed class Screen(val route: String, val icon: Any, val label: String) {

    data object HomeScreen :
        Screen(route = "homescreen", icon = R.drawable.baseline_maps_24, label = "Utforsk")

    data object InfoScreen :
        Screen(route = "infoscreen", icon = R.drawable.baseline_weather_24, label = "Været")

    data object TrackingScreen :
        Screen(route = "trackingscreen", icon = R.drawable.baseline_record, label = "Spor")

    data object RouteScreen :
        Screen(route = "routescreen", icon = R.drawable.baseline_route, label = "Ruter")

    data object ProfileScreen :
        Screen(route = "profilescreen", icon = Icons.Filled.Person, label = "Profil")

}


@Composable
fun loadDrawableAsImageVector(resourceId: Int): ImageVector {
    return ImageVector.vectorResource(id = resourceId)
}