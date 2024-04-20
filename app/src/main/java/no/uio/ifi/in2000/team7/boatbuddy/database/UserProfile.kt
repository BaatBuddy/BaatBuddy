package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//Vet ikke hvor entity class burde ligge

@Entity
data class UserProfile(
    val firstName: String,
    val lastName: String,
    @PrimaryKey(autoGenerate = true)
    val username: String? = null
)



