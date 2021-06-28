package com.leewilson.moviebee.model

/**
 * Data class used to add user to Firebase
 * Represents a new user.
 */
data class User(
    val uid: String,
    val displayName: String
)