package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(settingViewModel: SettingViewModel) {
    val settingUIState by settingViewModel.settingUIState.collectAsState()

    Scaffold() { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            contentAlignment = Alignment.Center
        ) {
            if (settingUIState.selectedUser == null) {
                CreateUserScreen(settingViewModel = settingViewModel)
            } else {
                UserProfileScreen(settingViewModel = settingViewModel)
            }

        }

    }
}

@Composable
fun UserProfileScreen(settingViewModel: SettingViewModel) {

    val settingUIState by settingViewModel.settingUIState.collectAsState()


    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column {
                Text(text = "Profil:")
                Text(text = settingUIState.name)
                Text(text = settingUIState.username)
            }
        }
    }
}

@Composable
fun CreateUserScreen(settingViewModel: SettingViewModel) {

    val settingUIState by settingViewModel.settingUIState.collectAsState()

    var invalidUsername by remember { mutableStateOf(false) }
    var invalidName by remember { mutableStateOf(false) }

    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            Column {

                // name
                OutlinedTextField(
                    value = settingUIState.name,
                    onValueChange = {
                        if (it.length > 20) return@OutlinedTextField
                        settingViewModel.updateName(it)
                    },
                    label = { Text(text = "Navn") },
                    isError = invalidName,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        invalidUsername = settingUIState.username.isBlank()
                        invalidName = settingUIState.name.isBlank()
                        if (!invalidUsername && !invalidName) {
                            settingViewModel.addUser(
                                username = settingUIState.username,
                                name = settingUIState.name
                            )
                        }
                    })
                )

                // username
                OutlinedTextField(
                    value = settingUIState.username,
                    onValueChange = {
                        if (it.length > 20) return@OutlinedTextField
                        settingViewModel.updateUsername(it)

                    },
                    label = { Text(text = "Brukernavn") },
                    isError = invalidUsername,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        invalidUsername = settingUIState.username.isBlank()
                        invalidName = settingUIState.name.isBlank()
                        if (!invalidUsername && !invalidName) {
                            settingViewModel.addUser(
                                username = settingUIState.username,
                                name = settingUIState.name
                            )
                            settingViewModel.selectUser(settingUIState.username)
                        }
                    })
                )


            }
        }
    }
}