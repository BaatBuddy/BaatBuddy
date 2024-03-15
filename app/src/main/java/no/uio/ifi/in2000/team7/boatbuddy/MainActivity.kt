package no.uio.ifi.in2000.team7.boatbuddy


import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap.OSMScreen
import no.uio.ifi.in2000.team7.boatbuddy.ui.theme.BoatbuddyTheme
import org.osmdroid.config.Configuration

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
                    Configuration.getInstance()
                        .load(this, PreferenceManager.getDefaultSharedPreferences(this));
                    val screen = OSMScreen()
                    screen.CreateMap()

                }
            }
        }
    }
}
