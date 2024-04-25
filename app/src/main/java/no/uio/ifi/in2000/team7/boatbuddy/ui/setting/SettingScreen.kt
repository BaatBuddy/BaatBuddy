package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingScreen(settingViewModel: SettingViewModel) {
    var username by remember { mutableStateOf("") }

    val settingUIState by settingViewModel.settingUIState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = username, onValueChange = { username = it })
            Button(onClick = {
                settingViewModel.getUser(username)
            }) {
                Text(text = "Log user")
            }
            Button(onClick = {
                settingViewModel.addUser(username)
            }) {
                Text(text = "Add")
            }
            Text(
                text = "User full name: ${settingUIState.selectedUser?.username} ${settingUIState.selectedUser?.firstName} ${settingUIState.selectedUser?.lastName}"
            )
        }
    }
}