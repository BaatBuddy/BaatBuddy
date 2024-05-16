package no.uio.ifi.in2000.team7.boatbuddy.data

import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData

object PolygonPosition {
    // checks if a geopoint is inside a alert polygon
    fun checkUserLocationAlertAreas(
        lon: Double,
        lat: Double,
        featureData: List<FeatureData>
    ): List<FeatureData> {
        return featureData.filter { fd ->
            fd.affectedArea.any { area ->
                area.any { polygon ->
                    checkUserLocationPolygon(points = polygon, lon = lon, lat = lat)
                }
            }
        }
    }

    // http://www.philliplemons.com/posts/ray-casting-algorithm#:~:text=The%20algorithm%20starts%20with%20P,as%20seen%20in%20Figure%202 + gpt
    fun checkUserLocationPolygon(
        points: List<List<Double>>,
        lon: Double,
        lat: Double
    ): Boolean {

        if (points.size < 3) return false // Not a polygon
        if (points.any { it[0] == lon && it[1] == lat }) return true

        var inside = false
        var prevPoint = points.last()
        for (point in points) {
            val x1 = prevPoint[0]
            val y1 = prevPoint[1]
            val x2 = point[0]
            val y2 = point[1]

            if ((y1 > lat) != (y2 > lat) &&
                (lon < (x2 - x1) * (lat - y1) / (y2 - y1) + x1)
            ) {
                inside = !inside
            }

            prevPoint = point
        }
        return inside
    }
}