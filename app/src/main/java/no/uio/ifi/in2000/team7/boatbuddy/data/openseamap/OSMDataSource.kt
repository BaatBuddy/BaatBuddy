package no.uio.ifi.in2000.team7.boatbuddy.data.openseamap

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource

class OSMDataSource {

    fun getTileSource(): OnlineTileSourceBase {

        val tileSource = XYTileSource(
            "OpenSeaMap",
            0, 18, 256, ".png",
            arrayOf("https://tiles.openseamap.org/seamark/")
        )

        return tileSource

    }

}