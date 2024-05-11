package no.uio.ifi.in2000.team7.boatbuddy.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.HomeViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.MapboxViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.home.UserLocationViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.MetAlertsViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.OceanForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.SunriseViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme
import java.util.concurrent.TimeUnit

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
                NavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    locationForecastViewModel = locationForecastViewModel,
                    mapboxViewModel = mapboxViewModel,
                    oceanforecastViewModel = oceanforecastViewModel,
                    metalertsViewModel = metalertsViewModel,
                    profileViewModel = profileViewModel,
                    sunriseViewModel = sunriseViewModel,
                    homeViewModel = homeViewModel,
                    infoScreenViewModel = infoScreenViewModel,
                    userLocationViewModel = userLocationViewModel,
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

