package com.leewilson.movienights.repository

/**
 * Data class used to add user to Firebase RTDB
 * Represents a new user.
 */
data class User(
    val uid: String,
    val name: String
)