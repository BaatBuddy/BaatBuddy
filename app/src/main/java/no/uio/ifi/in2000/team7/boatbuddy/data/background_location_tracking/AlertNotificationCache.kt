package no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking

import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData

object AlertNotificationCache {
    var featureData: List<FeatureData> = emptyList()
    var enteredAlerts: MutableSet<String> = mutableSetOf() // contains events
}