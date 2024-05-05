package no.uio.ifi.in2000.team7.boatbuddy.model

sealed class APIStatus {
    class Success(val data: Any) : APIStatus()

    object Loading : APIStatus()

    object Failed : APIStatus()
}