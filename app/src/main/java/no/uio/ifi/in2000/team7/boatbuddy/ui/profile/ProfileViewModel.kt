package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.database.BoatProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.database.Route
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.RouteRepository
import javax.inject.Inject

data class ProfileUIState(
    val users: List<UserProfile> = emptyList(),

    val selectedUser: UserProfile? = null,
    val username: String = "",
    val name: String = "",

    val boats: List<BoatProfile> = emptyList(),
    val selectedBoat: BoatProfile? = null,

    val routes: List<Route> = emptyList(),
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
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val routeRepository: RouteRepository
) : ViewModel() {

    private val _profileUIState = MutableStateFlow(ProfileUIState())
    val profileUIState: StateFlow<ProfileUIState> = _profileUIState

    private val _createUserUIState = MutableStateFlow(CreateUserUIState())
    val createUserUIState: StateFlow<CreateUserUIState> = _createUserUIState

    init {
        updateSelectedUser()
        getAllUsers()
    }


    fun updateUsername(username: String) {
        viewModelScope.launch {
            _profileUIState.update {
                it.copy(
                    username = username
                )
            }
        }
    }


    fun updateName(name: String) {
        viewModelScope.launch {
            _profileUIState.update {
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

    fun selectBoat(boatname: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.selectBoat(boatname = boatname, username = username)
            getAllBoatsUsername()
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = profileRepository.getUserByUsername(username = username)
            _profileUIState.update {
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
                _profileUIState.update {
                    it.copy(
                        selectedUser = user,
                        username = user.username,
                        name = user.name
                    )
                }
                updateSelectedBoat()
            }
        }
    }

    fun updateSelectedBoat() {
        viewModelScope.launch(Dispatchers.IO) {
            val boat =
                _profileUIState.value.selectedUser?.let {
                    profileRepository.getSelectedBoatUsername(
                        it.username
                    )
                }

            _profileUIState.update {
                it.copy(
                    selectedBoat = boat
                )
            }
        }
    }

    fun updateRoutes() {
        viewModelScope.launch(Dispatchers.IO) {
            val routes = _profileUIState.value.selectedUser?.let {
                routeRepository.getAllRoutesUsername(
                    username = it.username
                )
            } ?: emptyList()

            _profileUIState.update {
                it.copy(
                    routes = routes
                )
            }
        }
    }

    fun unselectUser() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.unselectUser()
            _profileUIState.update {
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
            _profileUIState.update {
                it.copy(
                    users = users
                )
            }
        }
    }

    fun getAllBoatsUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val boats =
                profileUIState.value.selectedUser?.let {
                    profileRepository.getAllBoatsUsername(
                        username = it.username
                    )
                } ?: emptyList()
            _profileUIState.update {
                it.copy(
                    boats = boats
                )
            }
        }
    }

    fun addBoat(
        username: String,
        boatname: String,
        boatSpeed: String,
        safetyDepth: String,
        safetyHeight: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.addBoat(
                username = username,
                boatname = boatname,
                boatSpeed = boatSpeed,
                safetyDepth = safetyDepth,
                safetyHeight = safetyHeight

            )
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

    // route

    fun addRouteToProfile(
        username: String,
        boatname: String,
        route: List<Point>,
        routename: String
    ) {
        viewModelScope.launch {
            routeRepository.addRouteUsername(
                username = username,
                boatname = boatname,
                route = route,
                routename = routename,
            )
        }
    }

}