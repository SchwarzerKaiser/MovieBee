package com.leewilson.movienights.ui.main.profile.state

sealed class ProfileStateEvent {

    object FetchUserData: ProfileStateEvent()

    data class UpdateUserData(
        var displayName: String? = null,
        var bio: String? = null
    )
}