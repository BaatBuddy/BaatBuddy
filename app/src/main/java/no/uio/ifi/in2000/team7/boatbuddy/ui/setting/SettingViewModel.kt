package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.setting.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastUiState
import javax.inject.Inject

data class SettingUIState(
    val selectedUser: UserProfile? = null
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _settingUIState = MutableStateFlow(SettingUIState())
    val settingUIState: StateFlow<SettingUIState> = _settingUIState


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

    fun addUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.addUserByUsername(username)
        }
    }

}