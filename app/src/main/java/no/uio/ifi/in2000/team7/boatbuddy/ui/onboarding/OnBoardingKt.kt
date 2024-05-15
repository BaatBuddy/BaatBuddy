package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun OnBoarding(
    onBoardingViewModel: OnBoardingViewModel,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {

    val onBoardingUIState by onBoardingViewModel.onBoardingUIState.collectAsState()

    Scaffold(
        bottomBar = {
            Column {
                LinearProgressIndicator(
                    progress = {
                        onBoardingUIState.progressValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    if (onBoardingUIState.index != 0) {
                        Button(
                            onClick = {
                                onBoardingViewModel.goToLastScreen()
                            },
                            modifier = Modifier
                                .weight(1f),
                        ) {
                            Text(text = "Forrige")
                        }
                    }
                    if (onBoardingUIState.index == 6) {
                        Button(
                            onClick = {
                                mainViewModel.updateShowOnBoarding(false)
                            },
                            modifier = Modifier
                                .weight(1f),
                        ) {
                            Text(text = "Ferdig")
                        }

                    } else if (onBoardingUIState.index == 1) {
                        Button(
                            onClick = {
                                onBoardingViewModel.goToNextScreen()
                            },
                            modifier = Modifier
                                .weight(1f),
                            enabled = onBoardingUIState.isDoneCreatingUser
                        ) {
                            Text(text = "Neste")
                        }
                    } else {
                        Button(
                            onClick = {
                                onBoardingViewModel.goToNextScreen()
                            },
                            modifier = Modifier
                                .weight(1f),
                        ) {
                            Text(text = if (onBoardingUIState.index == 0) "Kom i gang!" else "Neste")
                        }
                    }
                }

            }
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (onBoardingUIState.index) {
                    0 -> WelcomeScreen()
                    1 -> MakeUserScreen(
                        profileViewModel = profileViewModel,
                        navController = navController,
                        onBoardingViewModel = onBoardingViewModel
                    )

                    2 -> ExplainMakeRouteScreen()
                    3 -> ExplainGenerateRouteScreen()
                    4 -> ExplainWeatherScreen()
                    5 -> ExplainTrackingScreen()
                    6 -> FinishOnBoardingScreen()
                }
            }

        }
    }
}