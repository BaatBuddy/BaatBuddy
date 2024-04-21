package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserProfile(
    val firstName: String,
    val lastName: String,
    val username: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)



