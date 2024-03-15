package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap.OSMScreen
import org.osmdroid.config.Configuration

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) {

            }
        }

    ) {
        Configuration.getInstance()
            .load(
                LocalContext.current,
                PreferenceManager.getDefaultSharedPreferences(LocalContext.current) // TO-DO: fix
            );
        val screen = OSMScreen()
        screen.CreateMap()

    }

}