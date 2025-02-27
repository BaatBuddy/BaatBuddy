package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences

@Dao
interface UserProfileDao {

    @Upsert
    suspend fun insertUserProfile(profile: UserProfile)

    @Delete
    suspend fun deleteUserProfile(profile: UserProfile)

    // fetching users
    @Query("SELECT * FROM userprofile ORDER BY name")
    fun getUserProfilesOrderedByFirstName(): List<UserProfile>

    @Query("SELECT * FROM userprofile ORDER BY username")
    fun getUserProfilesOrderedByUserProfile(): List<UserProfile>

    @Query("SELECT * FROM userprofile")
    fun getAllUsers(): List<UserProfile>

    @Query("SELECT * FROM userprofile WHERE username LIKE :username ")
    fun getUserByUsername(username: String): UserProfile

    // deleting
    @Query("DELETE FROM userprofile WHERE username LIKE :username ")
    fun deleteUserByUsername(username: String)


    // selection
    @Query("UPDATE userprofile SET isSelected = 1 WHERE username LIKE :username")
    fun selectUser(username: String)

    @Query("SELECT * FROM userprofile WHERE isSelected")
    fun getSelectedUser(): UserProfile?

    @Query("UPDATE userprofile SET isSelected = 0")
    fun unselectUser()


    // tracking
    @Query("UPDATE userprofile SET isTracking = 1 WHERE username LIKE :username")
    fun startTrackingUsername(username: String)

    @Query("UPDATE userprofile SET isTracking = 0 WHERE username LIKE :username")
    fun stopTrackingUsername(username: String)

    // weather
    @Query("UPDATE userprofile SET preferences = :weatherPreferences WHERE isSelected")
    fun replaceWeatherPreferences(weatherPreferences: WeatherPreferences)

}