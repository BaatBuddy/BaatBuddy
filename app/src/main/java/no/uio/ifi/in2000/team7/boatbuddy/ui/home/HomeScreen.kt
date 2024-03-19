package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.MainActivity
import no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap.OSMScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, mainActivity: MainActivity) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) {
                Text(text = "ASDASD")
            }
        }
    ) {

        OSMScreen()
    }

}