package no.uio.ifi.in2000.team7.boatbuddy.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// dialog for asking user location permission
@Composable
fun LocationDialog(launchRequest: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Del din lokasjon") },
        text = { Text(text = "Deling av lokasjon er nødvendig for å vise været på din posisjon og sporing av ruter for egen bruk.") },
        confirmButton = {
            Button(onClick = {
                launchRequest()
                onDismiss()
            }) {
                Text(text = "Aktiver")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Ikke nå")
            }
        }
    )
}