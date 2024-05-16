package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BoatProfileDao {
    @Upsert
    suspend fun insertBoatProfile(boat: BoatProfile)

    @Delete
    suspend fun deleteBoatProfile(boat: BoatProfile)

    @Query("SELECT * FROM boatprofile WHERE username LIKE :username")
    suspend fun getAllBoatsUsername(username: String): List<BoatProfile>

    @Query("SELECT * FROM boatprofile WHERE username LIKE :username AND isSelected = 1")
    suspend fun getSelectedBoatUsername(username: String): BoatProfile

    @Query("UPDATE boatprofile SET isSelected = 0 WHERE username LIKE :username")
    suspend fun unselectBoatUsername(username: String)

    @Query("UPDATE boatprofile SET isSelected = 1 WHERE username LIKE :username AND boatname LIKE :boatname")
    suspend fun selectBoatUsername(boatname: String, username: String)

    @Query("SELECT * FROM boatprofile AS b INNER JOIN userprofile AS u ON (b.username = u.username) WHERE u.isSelected AND b.isSelected")
    suspend fun getSelectedBoatSelectedUser(): BoatProfile?
}
