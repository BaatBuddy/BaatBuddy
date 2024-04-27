package no.uio.ifi.in2000.team7.boatbuddy.data.setting

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

    suspend fun unselectUser() {
        userDao.unselectUser()
    }

    suspend fun getSelectedUser(): UserProfile? {
        return userDao.getSelectedUser()
    }

    suspend fun getAllUsers(): List<UserProfile> {
        return userDao.getAllUsers()
    }

}