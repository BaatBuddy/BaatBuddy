package no.uio.ifi.in2000.team7.boatbuddy.ui.profile

import android.util.Log
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
import no.uio.ifi.in2000.team7.boatbuddy.data.database.UserProfile
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.ProfileRepository
import no.uio.ifi.in2000.team7.boatbuddy.data.profile.RouteRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import no.uio.ifi.in2000.team7.boatbuddy.model.route.RouteMap
import javax.inject.Inject

data class ProfileUIState(
    val users: List<UserProfile> = emptyList(),

    val selectedUser: UserProfile? = null,
    val username: String = "",
    val name: String = "",

    val boats: List<BoatProfile> = emptyList(),
    val selectedBoat: BoatProfile? = null,
    val isSelectingBoat: Boolean = false,

    val selectedWeather: WeatherPreferences? = null,
)

data class CreateUserUIState(
    val name: String = "",
    val username: String = "",

    val boatname: String = "",
    val safetyHeight: String = "",
    val safetyDepth: String = "",
    val boatSpeed: String = "",
)

data class RouteScreenUIState(
    val routeMaps: List<RouteMap> = emptyList(),
    val routeName: String = "",
    val routeDescription: String = "",
    val currentRouteView: String? = null,
    val startTime: String = "",
    val finishTime: String = "",

    val selectedRouteMap: RouteMap? = null,
    val pickedRouteMap: RouteMap? = null, // user picks to have on map / have weather info on
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

    private val _routeScreenUIState = MutableStateFlow(RouteScreenUIState())
    val routeScreenUIState: StateFlow<RouteScreenUIState> = _routeScreenUIState

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
            updateSelectedUser()
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
        }
    }

    // use this one to update boats only too
    fun updateSelectedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = profileRepository.getSelectedUser()
            if (user != null) {
                _profileUIState.update {
                    it.copy(
                        selectedUser = user,
                        username = user.username,
                        name = user.name,
                        selectedWeather = user.preferences
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
            Log.i("ASDASD", _profileUIState.value.selectedUser.toString())
            Log.i("ASDASD", boat.toString())
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
            _routeScreenUIState.update {
                it.copy(
                    routeMaps = routes
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
        routename: String,
        routeDescription: String,
        route: List<Point>?
    ) {
        viewModelScope.launch {
            routeRepository.addRouteUsername(
                username = username,
                boatname = boatname,
                routename = routename,
                routeDescription = routeDescription,
                route = route,
            )
            _routeScreenUIState.update {
                it.copy(
                    routeName = "",
                    routeDescription = "",
                    currentRouteView = null,
                )
            }
        }
    }

    fun updateRouteName(routeName: String) {
        viewModelScope.launch {
            _routeScreenUIState.update {
                it.copy(
                    routeName = routeName
                )
            }
        }
    }

    fun updateCurrentRouteTime() {
        viewModelScope.launch {
            routeRepository.setFinalFinishTime()
            _routeScreenUIState.update {
                it.copy(
                    startTime = routeRepository.getStartTime(),
                    finishTime = routeRepository.getFinishTime()
                )
            }
        }
    }

    fun updateRouteDescription(routeDescription: String) {
        viewModelScope.launch {
            _routeScreenUIState.update {
                it.copy(
                    routeDescription = routeDescription
                )
            }
        }
    }

    fun updateSelectedRoute(routeMap: RouteMap?) {
        viewModelScope.launch(Dispatchers.IO) {
            _routeScreenUIState.update {
                it.copy(
                    selectedRouteMap = routeMap
                )
            }
        }
    }

    fun updatePickedRoute(routeMap: RouteMap?) {
        viewModelScope.launch(Dispatchers.IO) {
            _routeScreenUIState.update {
                it.copy(
                    pickedRouteMap = routeMap
                )
            }
        }
    }

    fun updateCurrentRoute(points: List<Point>? = null) {
        viewModelScope.launch {
            _routeScreenUIState.update {
                it.copy(
                    currentRouteView = routeRepository.getTemporaryRouteView(points = points)
                )
            }
        }
    }

    fun startSelectingBoats() {
        viewModelScope.launch {
            _profileUIState.update {
                it.copy(
                    isSelectingBoat = true
                )
            }
        }
    }

    fun stopSelectingBoats() {
        viewModelScope.launch {
            _profileUIState.update {
                it.copy(
                    isSelectingBoat = false
                )
            }
        }
    }

    fun updateWeatherPreference(weatherPreferences: WeatherPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            _profileUIState.update {
                it.copy(
                    selectedWeather = weatherPreferences
                )
            }
        }
    }


}