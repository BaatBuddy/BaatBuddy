package no.uio.ifi.in2000.team7.boatbuddy.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val mainViewModel: MainViewModel by viewModels()
    private val locationForecastViewModel: LocationForecastViewModel by viewModels()
    private val mapboxViewModel: MapboxViewModel by viewModels()
    private val metalertsViewModel: MetAlertsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userLocationViewModel: UserLocationViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val infoScreenViewModel: InfoScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.mainScreenUIState.value.splashScreenReady
            }
        }

        setContent {

            BoatbuddyTheme {
                navController = rememberNavController()
                val context = LocalContext.current
                // Internet connectivity
                val connectivityObserver = NetworkConnectivityObserver(context)
                val status by connectivityObserver.observe().collectAsState(
                    initial = NetworkConnectivityObserver.Status.NoStatus
                )
                NavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    locationForecastViewModel = locationForecastViewModel,
                    mapboxViewModel = mapboxViewModel,
                    metalertsViewModel = metalertsViewModel,
                    profileViewModel = profileViewModel,
                    homeViewModel = homeViewModel,
                    infoScreenViewModel = infoScreenViewModel,
                    userLocationViewModel = userLocationViewModel,
                    status = status
                )
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                mapboxViewModel.panToUser()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                mainViewModel.showLocationDialog()
            }

            else -> {
                mainViewModel.showLocationDialog()
            }
        }
    }
}

