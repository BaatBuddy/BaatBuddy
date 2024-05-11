package no.uio.ifi.in2000.team7.boatbuddy.ui.route

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel


@Composable
fun RouteCard(
    routeMap: RouteMap,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {

    val onClickEvent: () -> Unit = {
        profileViewModel.updateSelectedRoute(routeMap = routeMap)
        navController.navigate("routeinfo")
    }

    var loading by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                run(onClickEvent)
            }
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .padding(4.dp)
        ) {
            Column {
                Text(text = "Rute navn")
                Text(text = routeMap.route.routename)
                Log.i("ASDASD", routeMap.mapURL + " HGBWEHBGFHEWBGJ")
                AsyncImage(
                    model = routeMap.mapURL,
                    contentDescription = "Map with path",
                    modifier = Modifier
                        .fillMaxWidth(),
                    onError = {
                        loading = false
                    },
                    onSuccess = {
                        loading = false
                    }
                )
            }
            if (loading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator()

                }
            }
        }
    }
}