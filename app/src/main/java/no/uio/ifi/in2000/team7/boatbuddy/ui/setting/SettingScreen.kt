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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import no.uio.ifi.in2000.team7.boatbuddy.database.ProfileDatabase
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfileDao

@Composable
fun SettingScreen(settingViewModel: SettingViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    settingViewModel.initialize(LocalContext.current)
    
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
                Log.i("ASDASD", settingViewModel.getUser(username = username).toString())
            }) {
                Text(text = "Log user")
            }
        }
    }
}