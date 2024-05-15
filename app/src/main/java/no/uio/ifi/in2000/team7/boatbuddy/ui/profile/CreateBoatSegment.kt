package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.R

@Composable
fun CreateBoatSegment(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    val createUserUIState by profileViewModel.createUserUIState.collectAsState()

    val invalidMap = remember {
        mutableMapOf(
            "boatname" to false,
            "boatSpeed" to false,
            "safetyHeight" to false,
            "safetyDepth" to false
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
            value = createUserUIState.boatname,
            onValueChange = {
                if (it.length > 20) return@OutlinedTextField
                profileViewModel.updateBoatName(it)

            },
            label = { Text(text = "Båt navn") },
            isError = invalidMap["boatname"] ?: false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                checkInputsBoatProfile(
                    createUserUIState,
                    profileUIState,
                    invalidMap,
                    profileViewModel,
                    navController
                )
            })
        )
        //size
        Text(text = "Trykk på et av ikonene for å få raske verdier")
        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .size(72.dp)
                    .padding(4.dp)
                    .clickable {
                        profileViewModel.updateBoatSpeed("21")
                        profileViewModel.updateBoatHeight("2")
                        profileViewModel.updateBoatDepth("1")
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.boat_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                    )

                }

            }
            ElevatedCard(
                modifier = Modifier
                    .size(72.dp)
                    .padding(4.dp)
                    .clickable {
                        profileViewModel.updateBoatSpeed("14")
                        profileViewModel.updateBoatHeight("4")
                        profileViewModel.updateBoatDepth("2")
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.boatmedium_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                    )

                }

            }
            ElevatedCard(
                modifier = Modifier
                    .size(72.dp)
                    .padding(4.dp)
                    .clickable {
                        profileViewModel.updateBoatSpeed("7")
                        profileViewModel.updateBoatDepth("3")
                        profileViewModel.updateBoatHeight("6")
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.boatbig_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                    )

                }

            }

        }

        OutlinedTextField(
            value = createUserUIState.boatSpeed,
            onValueChange = {
                if (it.length > 20 && !it.isDigitsOnly()) return@OutlinedTextField
                profileViewModel.updateBoatSpeed(it)

            },
            label = { Text(text = "Båt hastighet") },
            isError = invalidMap["boatSpeed"] ?: false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                checkInputsBoatProfile(
                    createUserUIState,
                    profileUIState,
                    invalidMap,
                    profileViewModel,
                    navController
                )
            })
        )

        OutlinedTextField(
            value = createUserUIState.safetyHeight,
            onValueChange = {
                if (it.length > 20 && !it.isDigitsOnly()) return@OutlinedTextField
                profileViewModel.updateBoatHeight(it)

            },
            label = { Text(text = "Sikkerhets høyde på båten") },
            isError = invalidMap["safetyHeight"] ?: false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                checkInputsBoatProfile(
                    createUserUIState,
                    profileUIState,
                    invalidMap,
                    profileViewModel,
                    navController
                )
            })
        )

        OutlinedTextField(
            value = createUserUIState.safetyDepth,
            onValueChange = {
                if (it.length > 20 && it.isDigitsOnly()) return@OutlinedTextField
                profileViewModel.updateBoatDepth(it)

            },
            label = { Text(text = "Sikkerhets dybde på båten") },
            isError = invalidMap["safetyDepth"] ?: false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                checkInputsBoatProfile(
                    createUserUIState,
                    profileUIState,
                    invalidMap,
                    profileViewModel,
                    navController
                )
            })
        )


    }
}