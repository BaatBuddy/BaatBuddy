package no.uio.ifi.in2000.team7.boatbuddy.ui.openseamap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.uio.ifi.in2000.team7.boatbuddy.data.openseamap.OSMDataSource
import org.osmdroid.util.GeoPoint

class OSMViewModel : ViewModel() {

    val dataSource = OSMDataSource()
    val tileSource = dataSource.getTileSource()

    private val _mapCenter = MutableLiveData(GeoPoint(59.964363, 10.730983))
    val mapCenter: LiveData<GeoPoint> = _mapCenter

    private val _zoomLevel = MutableLiveData(17.0)
    val zoomLevel: LiveData<Double> = _zoomLevel

}