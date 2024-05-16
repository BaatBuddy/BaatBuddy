package no.uio.ifi.in2000.team7.boatbuddy.model

sealed class APIStatus {
    class Success(val data: Any) : APIStatus()

    data object Loading : APIStatus()

    data object Failed : APIStatus()
}