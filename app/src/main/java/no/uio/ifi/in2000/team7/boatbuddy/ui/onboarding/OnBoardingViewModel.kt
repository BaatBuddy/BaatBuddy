package no.uio.ifi.in2000.team7.boatbuddy.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.data.onboarding.OnboardingRepository
import javax.inject.Inject


data class OnBoardingUIState(
    val index: Int = 0,
    val progressValue: Float = 0f,
    val isDoneCreatingUser: Boolean = false,
    val showOnBoarding: Boolean = true,
)

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    private val _onBoardingUIState = MutableStateFlow(OnBoardingUIState())
    val onBoardingUIState = _onBoardingUIState.asStateFlow()

    init {
        updateShowOnBoarding(onboardingRepository.getOnboardingValue())
    }

    fun updateShowOnBoarding(state: Boolean) {
        viewModelScope.launch {
            _onBoardingUIState.update {
                it.copy(
                    showOnBoarding = state
                )
            }
            onboardingRepository.setOnboardingCompleted(false)
        }
    }

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

    private fun updateProgressValue() {
        viewModelScope.launch {
            _onBoardingUIState.update {
                it.copy(
                    progressValue = _onBoardingUIState.value.index.toFloat() / 6
                )
            }

        }
    }

    fun updateIsDoneCreatingUser(state: Boolean) {
        viewModelScope.launch {
            _onBoardingUIState.update {
                it.copy(
                    isDoneCreatingUser = state
                )
            }
        }
    }
}