package no.uio.ifi.in2000.team7.boatbuddy.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class InfoScreenUIState(
    val selectedTab: Int = 0,
)

class InfoScreenViewModel : ViewModel() {


    private val _infoScreenUIState = MutableStateFlow(InfoScreenUIState())
    val infoScreenUIState: StateFlow<InfoScreenUIState> = _infoScreenUIState.asStateFlow()

    fun selectTab(tabIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _infoScreenUIState.update {
                it.copy(
                    selectedTab = tabIndex
                )
            }
        }
    }
}