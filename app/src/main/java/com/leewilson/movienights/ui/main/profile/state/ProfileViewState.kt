package com.leewilson.movienights.ui.main.profile.state

data class ProfileViewState(
    val followers: Int = 0,
    val following: Int = 0,
    val displayName: String = "",
    val bio: String = ""
)