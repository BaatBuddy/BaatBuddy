package no.uio.ifi.in2000.team7.boatbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import no.uio.ifi.in2000.team7.boatbuddy.ui.oceanforecast.OceanForecastScreen
=======
import no.uio.ifi.in2000.team7.boatbuddy.ui.metalerts.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.sunrise.SunriseScreen
>>>>>>> 77eb9524bee7cbaf19379795cf1fa6e714aa1cbc
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoatbuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
<<<<<<< HEAD
                    OceanForecastScreen()
=======
                    Screen()
                    SunriseScreen()
>>>>>>> 77eb9524bee7cbaf19379795cf1fa6e714aa1cbc
                }
            }
        }
    }
}
