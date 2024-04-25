package no.uio.ifi.in2000.team7.boatbuddy.data.setting

import android.util.Log
import no.uio.ifi.in2000.team7.boatbuddy.database.ProfileDatabase
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfileDao
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userDao: UserProfileDao,
) {
    suspend fun getUserByUsername(username: String): UserProfile {
        return userDao.getUserByUsername(username = username)
    }

    suspend fun addUserByUsername(username: String, name: String) {
        return userDao.insertUserProfile(
            UserProfile(
                username = username,
                name = name,
                isSelected = true
            )
        )
    }

    suspend fun selectUser(username: String) {
        userDao.selectUser(username = username)
    }

    suspend fun getSelectedUser(): UserProfile {
        return userDao.getSelectedUser()
    }
}