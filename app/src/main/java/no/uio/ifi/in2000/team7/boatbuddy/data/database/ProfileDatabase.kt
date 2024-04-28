package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserProfile::class, BoatProfile::class], version = 4)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun userDao(): UserProfileDao
    abstract fun boatDao(): BoatProfileDao
}