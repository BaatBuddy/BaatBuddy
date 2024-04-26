package no.uio.ifi.in2000.team7.boatbuddy.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.base.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.sunrise.SunriseRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.sunrise.SunriseData

/*data class SettingUIState(
)

class SettingScreenViewModel : ViewModel() {

    private var initialized = false

    @MainThread
    fun initialize() {
        if (initialized) return

        initialized = true
    }

}*/