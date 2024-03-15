package no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay

class OSMScreen {

    val viewModel = OSMViewModel()

    @Composable
    fun CreateMap() {

        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

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
    }


}