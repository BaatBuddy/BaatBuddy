package no.uio.ifi.in2000.team7.boatbuddy.database

sealed interface UserProfileEvent {
    object SaveUserProfile: UserProfileEvent
    data class SetFirstName(val firstName: String) : UserProfileEvent
    data class SetLastName(val lastName: String) : UserProfileEvent
    data class SetUserName(val username: String) : UserProfileEvent
    object showDialog: UserProfileEvent
    object hideDialog: UserProfileEvent
    data class SortUserProfiles(val sortType: SortType): UserProfileEvent

    // Add data class to delete user profile ?


}