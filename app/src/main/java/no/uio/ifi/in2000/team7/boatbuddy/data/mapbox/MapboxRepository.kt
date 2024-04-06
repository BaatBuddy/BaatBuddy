package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import android.content.Context
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.MapView

interface MapboxRepo {
    fun createMap(context: Context): MapView
}

class MapboxRepository(
) : MapboxRepo {
    override fun createMap(context: Context): MapView {
        // consider a new solution for this
        MapboxOptions.accessToken =
            "pk.eyJ1IjoibWFmcmVkcmkiLCJhIjoiY2x1MWIxZ3Q2MGtlZDJrbnhmdTZ0NHZtaSJ9.B6Iawg2wbjSnGqMEOEtxvQ"

        return MapView(context)
    }


}