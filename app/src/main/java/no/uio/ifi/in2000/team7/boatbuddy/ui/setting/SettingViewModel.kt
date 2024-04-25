package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.setting.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfile
import javax.inject.Inject

data class SettingUIState(
    val selectedUser: UserProfile? = null,
    val username: String = "",
    val name: String = "",
    val boatProfiles: List<BoatProfile> = emptyList()
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _settingUIState = MutableStateFlow(SettingUIState())
    val settingUIState: StateFlow<SettingUIState> = _settingUIState

    init {
        updateSelectedUser()
    }


    fun updateUsername(username: String) {
        viewModelScope.launch {
            _settingUIState.update {
                it.copy(
                    username = username
                )
            }
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            _settingUIState.update {
                it.copy(
                    name = name
                )
            }
        }
    }

    fun selectUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.selectUser(username = username)
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = profileRepository.getUserByUsername(username = username)
            _settingUIState.update {
                it.copy(
                    selectedUser = user
                )
            }
        }
    }

    fun addUser(username: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.addUserByUsername(username = username, name = name)
            updateSelectedUser()
        }
    }

    fun updateSelectedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = profileRepository.getSelectedUser()
            _settingUIState.update {
                it.copy(
                    selectedUser = user,
                    username = user.username,
                    name = user.name
                )
            }
        }
    }

}