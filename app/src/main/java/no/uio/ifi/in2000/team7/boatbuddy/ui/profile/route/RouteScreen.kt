package no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    profileViewModel: ProfileViewModel
) {
    profileViewModel.updateRoutes()
    val profileUIState by profileViewModel.profileUIState.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Lagrede ruter")
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            LazyColumn(

            ) {
                /*
                profileUIState.routes.forEach {
                    Text(text = it.route.toString())
                }
                */
                val points1 = listOf(
                    Point.fromLngLat(10.01, 59.4),
                    Point.fromLngLat(10.02, 59.5),
                    Point.fromLngLat(10.03, 59.6),
                )

                val points2 = listOf(
                    Point.fromLngLat(11.01, 58.4),
                    Point.fromLngLat(11.02, 58.5),
                    Point.fromLngLat(11.03, 58.6),
                    Point.fromLngLat(11.3, 59.0),


                    )

                val mapRepo1 = MapboxRepository()
                val cameraOptions1 = CameraOptions.Builder()
                    .build()
                val mapView1 = mapRepo1.createMap(
                    context = context,
                    cameraOptions = cameraOptions1,
                    style = "mapbox://styles/mafredri/clu8bbhvh019501p71sewd7eg",
                )
                mapRepo1.fitPolylineToScreen(points1)

                val mapRepo2 = MapboxRepository()
                val cameraOptions2 = CameraOptions.Builder()
                    .build()
                val mapView2 = mapRepo2.createMap(
                    context = context,
                    cameraOptions = cameraOptions2,
                    style = "mapbox://styles/mafredri/clu8bbhvh019501p71sewd7eg",
                )
                mapRepo2.fitPolylineToScreen(points2)

                items(
                    listOf(
                        RouteMap(
                            route = Route(
                                username = "",
                                boatname = "",
                                routeID = 0,
                                routename = "",
                                route = points1,
                                start = "",
                                finish = ""
                            ),
                            mapView = mapView1
                        ),
                        RouteMap(
                            route = Route(
                                username = "",
                                boatname = "",
                                routeID = 0,
                                routename = "",
                                route = points2,
                                start = "",
                                finish = ""
                            ),
                            mapView = mapView2
                        )

                    )
                ) {
                    RouteCard(routeMap = it)
                }
            }
        }
    }
}