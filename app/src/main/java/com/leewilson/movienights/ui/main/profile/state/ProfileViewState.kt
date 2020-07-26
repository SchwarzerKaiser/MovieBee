package com.leewilson.movienights.ui.main.profile.state

import android.net.Uri

data class ProfileViewState(
    val followers: ArrayList<String> = ArrayList(),
    val following: ArrayList<String> = ArrayList(),
    val displayName: String = "",
    val bio: String = "",
    val email: String = "",
    val imageUri: String = ""
)
