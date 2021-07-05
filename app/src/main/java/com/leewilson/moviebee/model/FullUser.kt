package com.leewilson.moviebee.model

data class FullUser(
    val displayName: String? = null,
    val email: String? = null,
    val followers: List<String>? = emptyList(),
    val following: List<String>? = emptyList(),
    val imageUri: String? = null,
    val uid: String? = null,
    val bio: String? = null
)