package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(settingViewModel: SettingViewModel, navController: NavController) {

    val settingUIState by settingViewModel.settingUIState.collectAsState()

    var invalidUsername by remember { mutableStateOf(false) }
    var invalidName by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = "Lag profil") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Text(
                    text = "Bruker profil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(4.dp)
                )
                HorizontalDivider()

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

                Text(
                    text = "Båt profil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(4.dp)
                )
                HorizontalDivider()

                // boat
                // name
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
                //size
                Row() {
                    ElevatedCard(
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.boat_svgrepo_com),
                            contentDescription = "",
                            modifier = Modifier
                                .size(32.dp)
                        )

                    }
                    ElevatedCard(
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.boat_svgrepo_com),
                            contentDescription = "",
                            modifier = Modifier
                                .size(48.dp)
                        )

                    }
                    ElevatedCard(
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.boat_svgrepo_com),
                            contentDescription = "",
                            modifier = Modifier
                                .size(64.dp)
                        )

                    }

                }


            }
        }
    }
}