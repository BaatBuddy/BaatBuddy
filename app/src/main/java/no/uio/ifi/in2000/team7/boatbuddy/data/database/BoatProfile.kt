package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Entity

//finne primary key og foreign key
@Entity(primaryKeys = ["boatname", "username"])
data class BoatProfile(
    val boatname: String,
    val username: String,
    val safetyDepth: String,
    val safetyHeight: String,
    val boatSpeed: String,
    val isSelected: Boolean,
)
