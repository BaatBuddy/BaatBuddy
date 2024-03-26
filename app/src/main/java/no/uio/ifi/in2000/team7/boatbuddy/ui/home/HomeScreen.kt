package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox.MBScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
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

        MBScreen()

    }

}