package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.setting.SettingRepository
import no.uio.ifi.in2000.team7.boatbuddy.database.ProfileDatabase
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfileDao

/*data class SettingUIState(
)*/

@SuppressLint("StaticFieldLeak")
class SettingViewModel : ViewModel() {
    private val repository = SettingRepository()

    private var initialized = false
    
    fun initialize(context: Context) {
        if (initialized) return

        repository.initialize(context = context)

        initialized = true
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            repository.getUserByUsername(username = username)
        }
    }

}