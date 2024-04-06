package no.uio.ifi.in2000.team7.boatbuddy.ui.mapbox

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.test.espresso.base.MainThread
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.MapboxRepository


data class MapboxUIState(
    val mapView: MapView,
    val cameraOptions: CameraOptions,

    )

data class AnnotationUIState(
    val annotationApi: AnnotationPlugin,
    val pointAnnotationManager: PointAnnotationManager,
    val polylineAnnotationManager: PolylineAnnotationManager,
    val polygonAnnotationManager: PolygonAnnotationManager,
    val viewAnnotationManager: ViewAnnotationManager
)

class MapboxViewModel : ViewModel() {
    private val repository: MapboxRepository = MapboxRepository()

    private lateinit var _mapboxUIState: MutableStateFlow<MapboxUIState>
    lateinit var mapboxUIState: StateFlow<MapboxUIState>

    private var initialized = false

    @MainThread
    fun initialize(context: Context, cameraOptions: CameraOptions) {

        if (initialized) return

        initialized = true
        createMap(context = context, cameraOptions = cameraOptions)

    }


    private fun createMap(context: Context, cameraOptions: CameraOptions) {
        _mapboxUIState = MutableStateFlow(
            MapboxUIState(
                mapView = repository.createMap(context = context),
                cameraOptions = cameraOptions
            )
        )
        mapboxUIState = _mapboxUIState
    }

}