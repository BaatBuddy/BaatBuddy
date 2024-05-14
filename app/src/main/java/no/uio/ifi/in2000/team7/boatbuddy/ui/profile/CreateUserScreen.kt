package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(profileViewModel: ProfileViewModel, navController: NavController) {

    val createUserUIState by profileViewModel.createUserUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var (text, setText) = remember {
        mutableStateOf("Close keyboard on done ime action")
    }


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

        }
        , floatingActionButton = { ExtendedFloatingActionButton(
            onClick = {

                checkInputsUserProfile(
                    createUserUIState,
                    invalidMap,
                    profileViewModel,
                    navController
                )
                keyboardController?.hide()

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
        }}
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            Column {



                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)

                    )

                    // username
                    OutlinedTextField(
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
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        isError = invalidMap["username"] ?: false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)
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
                        value = createUserUIState.boatname,
                        onValueChange = {
                            if (it.length <= 20) {
                                profileViewModel.updateBoatName(it)
                            }
                        },
                        //colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.onPrimaryContainer),

                        maxLines = 1,
                        label = {
                            Text(
                                text = "Båt navn",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        isError = invalidMap["boatname"] ?: false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )

                    //size
                    Text(text = "Trykk på et av ikonene for å få standard verdier")
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
                            if (it.length <= 20 && it.isDigitsOnly()) {
                                profileViewModel.updateBoatSpeed(it)
                                text = it
                            }
                        },
                        maxLines = 1,
                        label = {
                            Text(
                                text = "Båt hastighet i knop",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        isError = invalidMap["boatSpeed"] ?: false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )


                    OutlinedTextField(
                        value = createUserUIState.safetyHeight,
                        onValueChange = {
                            if (it.length <= 20 && it.isDigitsOnly()) {
                                profileViewModel.updateBoatSpeed(it)
                                text = it
                            }
                        },
                        maxLines = 1,
                        label = {
                            Text(
                                text = "Sikkerhets høyde på båten",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        isError = invalidMap["safetyHeight"] ?: false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )


                    OutlinedTextField(
                        value = createUserUIState.safetyDepth,
                        onValueChange = {
                            if (it.length <= 20 && it.isDigitsOnly()) {
                                profileViewModel.updateBoatSpeed(it)
                                text = it
                            }

                        },
                        maxLines = 1,
                        label = {
                            Text(
                                text = "Sikkerhets dybde på båten",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        isError = invalidMap["safetyDepth"] ?: false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            checkInputsUserProfile(
                                createUserUIState,
                                invalidMap,
                                profileViewModel,
                                navController
                            )
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                        //  To
                    )

                    Spacer(modifier = Modifier.height(60.dp))




            }






            }


        }


    }

}

fun checkInputsUserProfile(
    createUserUIState: CreateUserUIState,
    invalidMap: MutableMap<String, Boolean>,
    profileViewModel: ProfileViewModel,
    navController: NavController
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