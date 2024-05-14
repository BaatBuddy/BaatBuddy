package no.uio.ifi.in2000.team7.boatbuddy.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp



@Composable
fun NoUserDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(onDismissRequest = onDismissRequest,

        icon = {
            Icon(
                imageVector = icon,
                contentDescription = "Dialog Icon",
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(60.dp)
                    .padding(bottom = 5.dp),

            )
        },

        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dialogText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )
            }

        },

        confirmButton = {
            Button(
                onClick = onConfirmation,
                modifier = Modifier,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(containerColor = MaterialTheme.colorScheme.onSurfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant, disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant, disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant) ,
                //modifier = Modifier.weight(1f)
            ) {

                    Text(
                        text = "Lag profil",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

            }
        },

        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.secondaryContainer, disabledContentColor = MaterialTheme.colorScheme.secondaryContainer, disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer) ,
                //modifier = Modifier.weight(1f)
                shape = RoundedCornerShape(8.dp),
            ) {

                Text(text = "Avbryt",
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,

    )
}