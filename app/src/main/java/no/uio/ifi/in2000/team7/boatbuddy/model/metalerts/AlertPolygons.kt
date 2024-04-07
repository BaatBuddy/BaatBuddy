package no.uio.ifi.in2000.team7.boatbuddy.model.metalerts

import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotation

data class AlertPolygon(
    val polygonAnnotation: PolygonAnnotation,
    val featureData: FeatureData
)

