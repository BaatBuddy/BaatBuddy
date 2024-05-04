package no.uio.ifi.in2000.team7.boatbuddy.data.database

sealed interface UserProfileEvent {
    data object SaveUserProfile : UserProfileEvent
    data class SetFirstName(val firstName: String) : UserProfileEvent
    data class SetLastName(val lastName: String) : UserProfileEvent
    data class SetUserName(val username: String) : UserProfileEvent
    data object ShowDialog : UserProfileEvent
    data object HideDialog : UserProfileEvent
    data class SortUserProfiles(val sortType: SortType) : UserProfileEvent

    // Add data class to delete user profile ?


}