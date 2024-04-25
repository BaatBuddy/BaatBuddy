package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//finne primary key og foreign key
@Entity(primaryKeys = ["boatname", "username"])
data class BoatProfile(
    @PrimaryKey(autoGenerate = true)
    val boatname: String,
    val username: String,
    val safetyDepth: Double,
    val safetyHeight: Double,
    val boatSpeed: Double
)
