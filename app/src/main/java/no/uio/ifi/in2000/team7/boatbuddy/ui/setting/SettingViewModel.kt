package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
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
    val users: List<UserProfile> = emptyList(),

    val selectedUser: UserProfile? = null,
    val username: String = "",
    val name: String = "",

    val boats: List<BoatProfile> = emptyList(),
    val selectedBoat: BoatProfile? = null,
)

data class CreateUserUIState(
    val name: String = "",
    val username: String = "",

    val boatname: String = "",
    val safetyHeight: String = "",
    val safetyDepth: String = "",
    val boatSpeed: String = "",
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _settingUIState = MutableStateFlow(SettingUIState())
    val settingUIState: StateFlow<SettingUIState> = _settingUIState

    private val _createUserUIState = MutableStateFlow(CreateUserUIState())
    val createUserUIState: StateFlow<CreateUserUIState> = _createUserUIState

    init {
        updateSelectedUser()
        getAllUsers()
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
            updateSelectedUser()
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

    fun addUser(
        username: String,
        name: String,
        boatname: String,
        boatSpeed: String,
        safetyDepth: String,
        safetyHeight: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.addUser(
                username = username,
                name = name,
                boatname = boatname,
                boatSpeed = boatSpeed,
                safetyDepth = safetyDepth,
                safetyHeight = safetyHeight
            )
            updateSelectedUser()
        }
    }

    fun updateSelectedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = profileRepository.getSelectedUser()
            if (user != null) {
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

    fun updateSelectedBoat(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val boat =
                profileRepository.getSelectedBoatUsername(username = username)

            _settingUIState.update {
                it.copy(
                    selectedBoat = boat
                )
            }
        }
    }

    fun unselectUser() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.unselectUser()
            _settingUIState.update {
                it.copy(
                    selectedUser = null,
                    username = "",
                    name = "",
                    boats = emptyList()
                )
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = profileRepository.getAllUsers()
            _settingUIState.update {
                it.copy(
                    users = users
                )
            }
        }
    }

    fun getAllBoatsUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val boats =
                settingUIState.value.selectedUser?.let {
                    profileRepository.getAllBoatsUsername(
                        username = it.username
                    )
                } ?: emptyList()
            _settingUIState.update {
                it.copy(
                    boats = boats
                )
            }
        }
    }

    // create user UI state
    fun updateCreateUsername(username: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    username = username
                )
            }
        }
    }

    fun updateCreateName(name: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    name = name
                )
            }
        }
    }

    fun updateBoatName(name: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    boatname = name
                )
            }
        }
    }

    fun updateBoatHeight(height: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    safetyHeight = height
                )
            }
        }
    }

    fun updateBoatDepth(depth: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    safetyDepth = depth
                )
            }
        }
    }

    fun updateBoatSpeed(speed: String) {
        viewModelScope.launch {
            _createUserUIState.update {
                it.copy(
                    boatSpeed = speed
                )
            }
        }
    }

    fun clearCreateProfile() {
        viewModelScope.launch {
            _createUserUIState.update {
                CreateUserUIState()
            }
        }
    }

}