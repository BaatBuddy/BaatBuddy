package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences

@Entity
data class UserProfile(
    @PrimaryKey
    val username: String,
    val name: String,
    val isSelected: Boolean,
    val isTracking: Boolean,
    val preferences: WeatherPreferences,
)



