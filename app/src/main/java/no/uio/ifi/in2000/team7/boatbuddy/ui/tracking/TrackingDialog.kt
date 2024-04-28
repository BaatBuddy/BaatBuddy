package no.uio.ifi.in2000.team7.boatbuddy.ui.tracking

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingDialog(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    Dialog(
        onDismissRequest = {
            mainViewModel.hideDialog()
        },
    ) {

        Column {
            Text(text = "ASDASDASD")
        }

    }
}