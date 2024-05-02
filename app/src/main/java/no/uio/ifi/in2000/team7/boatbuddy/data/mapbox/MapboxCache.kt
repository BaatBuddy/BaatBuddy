package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox

import com.mapbox.geojson.Point

object MapboxCache {
    var savedMapURL: Map<List<Point>, String> = mapOf()

}