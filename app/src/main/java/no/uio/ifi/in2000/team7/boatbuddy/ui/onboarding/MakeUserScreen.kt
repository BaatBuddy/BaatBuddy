package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import CreateBoatSegment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.CreateUserSegment
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun MakeUserScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel,
) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()

    if (profileUIState.selectedUser != null) {
        onBoardingViewModel.updateIsDoneCreatingUser(true)
    } else {
        onBoardingViewModel.updateIsDoneCreatingUser(false)
    }

    Scaffold { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxWidth()
        ) {
            Column {
                CreateUserSegment(
                    profileViewModel = profileViewModel,
                    navController = navController
                )
            }
        }
    }
}