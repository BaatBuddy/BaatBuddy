package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap

@Composable
fun RouteWeatherInfo(
    routeMap: RouteMap,
    locationForecastViewModel: LocationForecastViewModel
) {
    val locationForecastUIState by locationForecastViewModel.locationForecastUiState.collectAsState()
    var loading by remember { mutableStateOf(true) }


    locationForecastViewModel.loadWeekdayForecastRoute(
        routeMap.route.route
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
//            .verticalScroll(rememberScrollState())
    ) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            if (locationForecastUIState.weekdayForecastRoute != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                ) {

                    locationForecastUIState.weekdayForecastRoute?.days?.let {
                        it.toList().sortedBy { pair ->
                            pair.second.date
                        }.forEach { tld ->
                            LocationCard(
                                dayForecast = tld.second,
                                selectedDay = locationForecastUIState.selectedDayRoute,
                                changeDay = { locationForecastViewModel.updateSelectedDayRoute(tld.second) },
                            )

                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    locationForecastUIState.selectedDayRoute?.let { LocationTable(dayForecast = it) }
                }
            }

        }
    }


    if (locationForecastUIState.weekdayForecastRoute == null) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
    }

}