package no.uio.ifi.in2000.team7.boatbuddy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team7.boatbuddy.data.UserLocation
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // requests user location to a object
        UserLocation.initialize(this)
        //UserLocation.checkPermissions()

        setContent {

            BoatbuddyTheme {
                // A surface container using the 'background' color from the theme\
                navController = rememberNavController()
                NavGraph(navController = navController)

                //MBScreen()

                /*Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                )



                }*/
            }
        }
    }
}
