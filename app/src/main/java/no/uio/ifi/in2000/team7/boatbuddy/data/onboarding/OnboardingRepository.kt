package no.uio.ifi.in2000.team7.boatbuddy.data.onboarding

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    @ApplicationContext context: Context,
) {

    // check if is first time
    private val preferences =
        context.getSharedPreferences("firstStart", Context.MODE_PRIVATE)


    fun getOnboardingValue(): Boolean {
        return preferences.getBoolean("firstStart", true)
    }

    fun setOnboardingCompleted(state: Boolean) {
        preferences.edit()
            .putBoolean("firstStart", state)
            .apply()
    }
}