package no.uio.ifi.in2000.team7.boatbuddy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mapbox.maps.MapboxExperimental
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BoatbuddyTheme {
                // A surface container using the 'background' color from the theme\
                navController = rememberNavController()
                NavGraph(navController = navController, mainActivity = this)

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
