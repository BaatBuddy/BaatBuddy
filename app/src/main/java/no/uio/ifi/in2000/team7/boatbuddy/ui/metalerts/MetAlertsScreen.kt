package no.uio.ifi.in2000.team7.boatbuddy.ui.metalerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData


@Composable
fun Screen (metAlertsViewModel: MetAlertsViewModel = viewModel()){

    val metAlertsUIState by metAlertsViewModel.metalertsUIState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth()
        .padding(35.dp)) {



        Text(text = metAlertsUIState.metalerts.toString(), modifier = Modifier.verticalScroll(
            rememberScrollState()))

    }

}

@Composable
fun MetCard(modifier: Modifier, metalerts: MetAlertsData) {

}