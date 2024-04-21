package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//finne primary key og foreign key
@Entity
data class BoatProfile (
    @PrimaryKey(autoGenerate = true)
    val boatName: String,
    val safetyDepth: Double,
    val safetyHeight: Double,
    val boatSpeed: Double
)
