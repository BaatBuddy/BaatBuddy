package no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    profileViewModel: ProfileViewModel
) {
    profileViewModel.updateRoutes()
    val routeScreenUIState by profileViewModel.routeScreenUIState.collectAsState()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Lagrede ruter")
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            LazyColumn(

            ) {
                items(routeScreenUIState.routeMaps) {
                    RouteCard(routeMap = it)
                }
            }
        }
    }
}