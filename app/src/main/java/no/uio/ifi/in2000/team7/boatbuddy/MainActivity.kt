package no.uio.ifi.in2000.team7.boatbuddy


import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mapbox.maps.MapboxExperimental
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

class MainActivity : ComponentActivity() {

            private lateinit var navController: NavHostController
            private val viewModel by viewModels<MainViewModel>()

            @OptIn(MapboxExperimental::class)
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                installSplashScreen().apply {
                    setKeepOnScreenCondition{
                        !viewModel.isReady.value
                    }

                }
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

