package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import javax.inject.Inject

data class HomeScreenUIState(
    val showBottomSheet: Boolean = false,
    val showBottomSheetInitialized: Boolean = false,
    val showExplanationCard: Boolean = false,
    val showWeatherAlertInfoCard: FeatureData? = null,
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val _homeScreenUIState: MutableStateFlow<HomeScreenUIState> =
        MutableStateFlow(HomeScreenUIState())
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState


    fun showBottomSheet() {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(
                    showBottomSheet = true,
                    showBottomSheetInitialized = true,
                )
            }
        }
    }

    fun hideBottomSheet() {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(
                    showBottomSheet = false
                )
            }
        }
    }

    fun resetBottomSheet() {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(
                    showBottomSheetInitialized = false
                )
            }
        }
    }

    fun updateShowExplanationCard(state: Boolean) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(
                    showExplanationCard = state
                )
            }
        }
    }

    fun updateShowWeatherAlertInfoCard(featureData: FeatureData?) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(
                    showWeatherAlertInfoCard = featureData
                )
            }
        }
    }
}