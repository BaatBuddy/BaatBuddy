package no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay


class MapViewDisposable(val mapView: MapView)

@Composable
fun OSMScreen(
    osmViewModel: OSMViewModel = viewModel()
) {


    val map = rememberMapView()
    lateinit var mapController: IMapController
    var marker: Marker? = null

    DisposableEffect(MapViewDisposable(map)) {
        map.onResume()

        onDispose {
            map.onPause()
        }
    }
    Column(
         modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    val tileProvider = MapTileProviderBasic(context, osmViewModel.tileSource)
                    val tilesOverlay = TilesOverlay(tileProvider, context)
                    map.overlays.add(tilesOverlay)
                    map.setTileSource(TileSourceFactory.MAPNIK)
                    map.setMultiTouchControls(true)
                    mapController = map.controller
                    osmViewModel.zoomLevel.value?.let { mapController.setZoom(it) }
                    mapController.setCenter(osmViewModel.mapCenter.value);
                    map
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

    return mapView
}


/*@Composable
fun CreateMap(mainActivity: MainActivity) {

    val mainContext = mainActivity.baseContext
    getInstance().load(mainContext, PreferenceManager.getDefaultSharedPreferences(mainContext))

    mainActivity.setContentView(R.layout.activity_main)

    map = mainActivity.findViewById<MapView>(R.id.map)
    map.setTileSource(TileSourceFactory.MAPNIK)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {

                val tileSource = viewModel.tileSource

                val tileProvider = MapTileProviderBasic(context)
                tileProvider.tileSource = tileSource

                val tilesOverlay = TilesOverlay(tileProvider, context)
                overlays.add(tilesOverlay)

                viewModel.zoomLevel.value?.let { controller.setZoom(it) }
                controller.setCenter(viewModel.mapCenter.value)

            }
        }
    )
}*/


