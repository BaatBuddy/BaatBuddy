package no.uio.ifi.in2000.team7.boatbuddy

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.uio.ifi.in2000.team7.boatbuddy.ui.BottomBar
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(navController: NavHostController) {

    Scaffold(bottomBar = { BottomBar(navController) }) {

        NavHost(
            navController = navController, startDestination = Screen.HomeScreen.route
        ) {
            composable(route = Screen.HomeScreen.route) {
                HomeScreen()
            }
            composable(route = Screen.InfoScreen.route) {
                InfoScreen()
            }
        }
    }
}


