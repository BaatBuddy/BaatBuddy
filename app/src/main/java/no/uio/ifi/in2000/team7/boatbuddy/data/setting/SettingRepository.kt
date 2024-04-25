package no.uio.ifi.in2000.team7.boatbuddy.data.setting

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.espresso.base.MainThread
import no.uio.ifi.in2000.team7.boatbuddy.database.ProfileDatabase
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfileDao

class SettingRepository {

    private lateinit var userDB: ProfileDatabase
    private lateinit var userDao: UserProfileDao

    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) return

        userDB = Room.databaseBuilder(
            context,
            ProfileDatabase::class.java, "users"
        ).build()
        userDao = userDB.userDao()
    }

    fun getUserByUsername(username: String) {
        Log.i("ASDASD", userDao.getUserByUsername(username = username).toString())
    }
}