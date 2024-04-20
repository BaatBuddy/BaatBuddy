package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Entity

//finne primary key og foreign key
@Entity
data class BoatProfile (
    val boatName: String,
    val safetyDepth: Double,
    val safetyHeight: Double,
    val boatSpeed: Double
)
