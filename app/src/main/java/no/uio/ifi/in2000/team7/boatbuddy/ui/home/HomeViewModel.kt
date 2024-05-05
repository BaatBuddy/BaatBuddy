package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class HomeScreenUIState(
    val showNotificationDialog: Boolean = false,
    val showLocationDialog: Boolean = false,
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _homeScreenUIState: MutableStateFlow<HomeScreenUIState> =
        MutableStateFlow(HomeScreenUIState())
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState


    init {

    }


}