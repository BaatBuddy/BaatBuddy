package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Upsert
    suspend fun insertUserProfile(profile: UserProfile)

    @Delete
    suspend fun deleteUserProfile(profile: UserProfile)

    @Query("SELECT * FROM userprofile ORDER BY firstName")
    fun getUserProfilesOrderedByFirstName(): Flow<List<UserProfile>>

    @Query("SELECT * FROM userprofile ORDER BY lastName")
    fun getUserProfilesOrderedByLastName(): Flow<List<UserProfile>>

    @Query("SELECT * FROM userprofile ORDER BY username")
    fun getUserProfilesOrderedByUserProfile(): Flow<List<UserProfile>>


}