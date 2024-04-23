package no.uio.ifi.in2000.team7.boatbuddy.data.mapbox.autoroute

import com.mapbox.geojson.Point


fun main() {
    var base: String = "https://webapp-no.skippo.io/api/autoroute?usehydrographica=false&"
    var course: String = "course="
    var end: String = "&safetydepth=5" +
            "&safetyheight=5" +
            "&boatspeed=5"
    var flag: Boolean = true
    var liste = listOf<Point>(
        Point.fromLngLat(10.607909077722013, 59.856108702935046),
        Point.fromLngLat(10.607421841197663, 59.860213589239464)
    )

    liste.forEach {

        if (flag) {
            course += it.longitude().toString()
            course += "%2C" + it.latitude().toString()
            flag = false
        }

        course += "%3B" + it.longitude().toString()
        course += "%2C" + it.latitude().toString()


    }

    println(base + course + end)


}