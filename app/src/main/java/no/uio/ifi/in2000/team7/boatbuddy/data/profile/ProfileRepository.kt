package no.uio.ifi.in2000.team7.boatbuddy.data.profile

import android.content.Context
import android.content.Intent
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.points
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.LocationService
import no.uio.ifi.in2000.team7.boatbuddy.data.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.database.BoatProfileDao
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfileDao
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userDao: UserProfileDao,
    private val boatDao: BoatProfileDao,
    private val context: Context,
) {
    suspend fun getUserByUsername(username: String): UserProfile {
        return userDao.getUserByUsername(username = username)
    }

    suspend fun addUser(
        username: String,
        name: String,
        boatname: String,
        boatSpeed: String,
        safetyDepth: String,
        safetyHeight: String
    ) {
        userDao.insertUserProfile(
            UserProfile(
                username = username,
                name = name,
                isSelected = true,
                isTracking = false,
                preferences = WeatherPreferences()
            )
        )
        boatDao.insertBoatProfile(
            BoatProfile(
                boatname = boatname,
                username = username,
                boatSpeed = boatSpeed,
                safetyDepth = safetyDepth,
                safetyHeight = safetyHeight,
                isSelected = true
            )
        )
    }

    suspend fun addBoat(
        username: String,
        boatname: String,
        boatSpeed: String,
        safetyDepth: String,
        safetyHeight: String,
    ) {
        boatDao.insertBoatProfile(
            BoatProfile(
                boatname = boatname,
                username = username,
                safetyDepth = safetyDepth,
                safetyHeight = safetyHeight,
                boatSpeed = boatSpeed,
                isSelected = false

            )
        )
    }

    suspend fun startTrackingUser() {
        val username = getSelectedUser()?.username
        if (username != null) {
            userDao.startTrackingUsername(username = username)
        }
    }

    suspend fun stopTrackingUser() {
        val username = getSelectedUser()?.username
        if (username != null) {
            userDao.stopTrackingUsername(username = username)
        }

        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context.startService(this)
        }
    }

    suspend fun selectUser(username: String) {
        userDao.selectUser(username = username)
    }

    suspend fun selectBoat(boatname: String, username: String) {
        unselectBoatUsername(username = username)
        boatDao.selectBoatUsername(boatname = boatname, username = username)
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

    suspend fun getAllBoatsUsername(username: String): List<BoatProfile> {
        return boatDao.getAllBoatsUsername(username = username)
    }

    suspend fun getSelectedBoatUsername(username: String): BoatProfile {
        return boatDao.getSelectedBoatUsername(username = username)
    }

    suspend fun selectBoatUsername(boatname: String, username: String) {
        boatDao.selectBoatUsername(boatname = boatname, username = username)
    }

    suspend fun unselectBoatUsername(username: String) {
        boatDao.unselectBoatUsername(username = username)
    }

}