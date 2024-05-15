package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Lag profil") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )

        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {

//                    checkInputsUserProfile(
//                        createUserUIState,
//                        invalidMap,
//                        profileViewModel,
//                        navController
//                    )
//                    keyboardController?.hide()

                }
            ) {


                Text(text = "Lag profil")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Create profile",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(32.dp)
                )
            }
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column {
                CreateUserSegment(
                    profileViewModel = profileViewModel,
                    navController = navController
                )

                CreateBoatSegment(
                    profileViewModel = profileViewModel,
                    navController = navController
                )
            }

        }


    }

}

fun checkInputsUserProfile(
    createUserUIState: CreateUserUIState,
    invalidMap: MutableMap<String, Boolean>,
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val username = createUserUIState.username
    val name = createUserUIState.name
    val boatname = createUserUIState.boatname
    val boatSpeed = createUserUIState.boatSpeed
    val safetyDepth = createUserUIState.safetyDepth
    val safetyHeight = createUserUIState.safetyHeight

    invalidMap["username"] = username.isBlank()
    invalidMap["name"] = name.isBlank()
    invalidMap["boatname"] = boatname.isBlank()
    invalidMap["boatSpeed"] = boatSpeed.isBlank()
    invalidMap["safetyDepth"] = safetyDepth.isBlank()
    invalidMap["safetyHeight"] = safetyHeight.isBlank()

    Log.i("ASDASD", invalidMap.toString())
    if (invalidMap.all { !it.value }) {
        profileViewModel.addUser(
            username = username,
            name = name,
            boatname = boatname,
            boatSpeed = boatSpeed,
            safetyDepth = safetyDepth,
            safetyHeight = safetyHeight
        )

        profileViewModel.clearCreateProfile()
        navController.popBackStack()
    }
}