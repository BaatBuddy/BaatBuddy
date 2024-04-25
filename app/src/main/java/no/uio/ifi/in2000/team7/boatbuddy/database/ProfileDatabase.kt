package no.uio.ifi.in2000.team7.boatbuddy.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserProfile::class], version = 1)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun userDao(): UserProfileDao
}