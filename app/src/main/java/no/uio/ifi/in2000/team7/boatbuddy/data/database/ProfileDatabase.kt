package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserProfile::class, BoatProfile::class, Route::class], version = 6)
@TypeConverters(PointConverter::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun userDao(): UserProfileDao
    abstract fun boatDao(): BoatProfileDao
    abstract fun routeDao(): RouteDao
}