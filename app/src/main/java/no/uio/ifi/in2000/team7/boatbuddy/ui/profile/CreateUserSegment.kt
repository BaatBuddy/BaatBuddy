package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CreateUserSegment(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val createUserUIState by profileViewModel.createUserUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val invalidMap = remember {
        mutableMapOf(
            "username" to false,
            "name" to false,
            "boatname" to false,
            "boatSpeed" to false,
            "safetyHeight" to false,
            "safetyDepth" to false
        )
    }


    Column {


        Spacer(modifier = Modifier.height(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Du må skrive inn et navn og et unikt brukernavn. I tillegg må du legge inn minst en båt for å lage en rute.",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
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
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary
                ),

                value = createUserUIState.name,
                onValueChange = {
                    if (it.length <= 20) {
                        profileViewModel.updateCreateName(it)
                    }
                },
                maxLines = 1,
                label = {
                    Text(
                        text = "Navn",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                isError = invalidMap["name"] ?: false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    checkInputsUserProfile(
                        createUserUIState = createUserUIState,
                        invalidMap = invalidMap,
                        profileViewModel = profileViewModel,
                        navController = navController
                    )
                }),
                modifier = Modifier
                    .focusRequester(focusRequester)

            )

            // username
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary
                ),
                value = createUserUIState.username,
                onValueChange = {
                    if (it.length <= 20) {
                        profileViewModel.updateCreateUsername(it)
                    }
                },
                maxLines = 1,
                label = {
                    Text(
                        text = "Brukernavn",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                isError = invalidMap["username"] ?: false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    checkInputsUserProfile(
                        createUserUIState = createUserUIState,
                        invalidMap = invalidMap,
                        profileViewModel = profileViewModel,
                        navController = navController
                    )
                }),
                modifier = Modifier
                    .focusRequester(focusRequester)

            )

        }

    }

    CreateBoatSegment(
        profileViewModel = profileViewModel,
        navController = navController,
        invalidMapIn = invalidMap,
        checkFunc = {
            checkInputsUserProfile(
                createUserUIState = createUserUIState,
                invalidMap = invalidMap,
                profileViewModel = profileViewModel,
                navController = navController
            )
        }
    )


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