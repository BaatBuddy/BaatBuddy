package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class OnBoardingUIState(
    val index: Int = 0,
    val progressValue: Float = 0f,
)

class OnBoardingViewModel : ViewModel() {

    private val _onBoardingUIState = MutableStateFlow(OnBoardingUIState())
    val onBoardingUIState = _onBoardingUIState.asStateFlow()


    fun goToLastScreen() {
        viewModelScope.launch {
            _onBoardingUIState.update {
                it.copy(
                    index = it.index - 1
                )
            }
            updateProgressValue()
        }
    }

    fun goToNextScreen() {
        viewModelScope.launch {
            _onBoardingUIState.update {
                it.copy(
                    index = it.index + 1
                )
            }
            updateProgressValue()
        }
    }

    fun updateProgressValue() {
        viewModelScope.launch(Dispatchers.IO) {
            _onBoardingUIState.update {
                it.copy(
                    progressValue = _onBoardingUIState.value.index.toFloat() / 2
                )
            }

        }
    }
}