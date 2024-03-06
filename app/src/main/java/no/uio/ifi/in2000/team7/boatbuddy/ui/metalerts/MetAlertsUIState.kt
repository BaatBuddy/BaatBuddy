package no.uio.ifi.in2000.team7.boatbuddy.ui.metalerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.metalerts.MetAlertsRepository
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.MetAlertsData

data class MetAlertsUIState(
    val metalerts : MetAlertsData?
)

class MetAlertsViewModel : ViewModel() {

    private val repository: MetAlertsRepository = MetAlertsRepository()

    private val _metalertsUIState = MutableStateFlow(MetAlertsUIState(null))
    val metalertsUIState: StateFlow<MetAlertsUIState> = _metalertsUIState.asStateFlow()

    init {
        loadmetalerts()

    }

    private fun loadmetalerts() {

        viewModelScope.launch(Dispatchers.IO) {

            val metalerts = repository.getMetAlertsData()
            _metalertsUIState.update { it.copy(metalerts = metalerts) }

        }

    }
}
