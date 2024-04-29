package no.uio.ifi.in2000.team7.boatbuddy.ui.profile.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap


@OptIn(MapboxExperimental::class)
@Composable
fun RouteCard(routeMap: RouteMap) {
    Card(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .padding(4.dp)
        ) {
            Column {
                Text(text = "Rute info jada jada")

                AndroidView(
                    factory = { ctx ->
                        routeMap.mapView
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                )
            }
        }
    }
}