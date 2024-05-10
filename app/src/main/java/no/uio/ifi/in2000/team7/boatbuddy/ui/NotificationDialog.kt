package no.uio.ifi.in2000.team7.boatbuddy.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NotificationDialog(navigateToSettings: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Aktiver varsling") },
        text = { Text(text = "Varsling er nødvendig for å advare om værfarer. Vil du aktivere varsling?") },
        confirmButton = {
            Button(onClick = {
                navigateToSettings()
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