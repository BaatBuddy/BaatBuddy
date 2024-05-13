package no.uio.ifi.in2000.team7.boatbuddy.ui.profile


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectUserScreen(profileViewModel: ProfileViewModel, navController: NavController) {
    val profileUIState by profileViewModel.profileUIState.collectAsState()
    profileViewModel.getAllUsers()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Velg profil") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("createuser")
                }
            ) {


                Text(text = "Lag profil")
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create profile",
                    tint = MaterialTheme.colorScheme.secondary,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    items(profileUIState.users) {
                        UserCard(user = it, profileViewModel = profileViewModel)
                    }
                }


            }
        }
    }
}