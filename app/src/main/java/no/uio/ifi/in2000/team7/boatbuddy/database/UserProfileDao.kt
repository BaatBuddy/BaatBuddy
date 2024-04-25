package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserProfileDao {

    @Upsert
    suspend fun insertUserProfile(profile: UserProfile)

    @Delete
    suspend fun deleteUserProfile(profile: UserProfile)

    @Query("SELECT * FROM userprofile ORDER BY name")
    fun getUserProfilesOrderedByFirstName(): List<UserProfile>

    @Query("SELECT * FROM userprofile ORDER BY username")
    fun getUserProfilesOrderedByUserProfile(): List<UserProfile>

    @Query("SELECT * FROM userprofile")
    fun getAllUsers(): List<UserProfile>

    @Query("SELECT * FROM userprofile WHERE username LIKE :username ")
    fun getUserByUsername(username: String): UserProfile

    @Query("DELETE FROM userprofile WHERE username LIKE :username ")
    fun deleteUserByUsername(username: String)

    @Query("SELECT * FROM userprofile WHERE isSelected")
    fun getSelectedUser(): UserProfile?

    @Query("UPDATE userprofile SET isSelected = true WHERE username LIKE :username")
    fun selectUser(username: String)

    @Query("UPDATE userprofile SET isSelected = false")
    fun unselectUser()

}