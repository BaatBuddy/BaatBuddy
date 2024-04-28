package no.uio.ifi.in2000.team7.boatbuddy.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.team7.boatbuddy.NavGraph
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.GetUserLocation
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.OceanForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.SunriseViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val mainViewModel: MainViewModel by viewModels()
    private val locationForecastViewModel: LocationForecastViewModel by viewModels()
    private val mapboxViewModel: MapboxViewModel by viewModels()
    private val metalertsViewModel: MetAlertsViewModel by viewModels()
    private val oceanforecastViewModel: OceanForecastViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val sunriseViewModel: SunriseViewModel by viewModels()
    private val userLocationViewModel: UserLocationViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.mainScreenUIState.value.splashScreenReady
            }
        }

        setContent {

            // fetch premissions for userlocation
            GetUserLocation()

            BoatbuddyTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    locationForecastViewModel = locationForecastViewModel,
                    mapboxViewModel = mapboxViewModel,
                    oceanforecastViewModel = oceanforecastViewModel,
                    metalertsViewModel = metalertsViewModel,
                    profileViewModel = profileViewModel,
                    sunriseViewModel = sunriseViewModel,
                    userLocationViewModel = userLocationViewModel,
                    homeViewModel = homeViewModel,
                )

            }

        }
    }

}




