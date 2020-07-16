package com.leewilson.movienights.util

class Constants {

    companion object {

        const val USER_UPDATE_FAILED = "Failed to update user. Please check your connection."

        // Status on updating Firestore user
        const val USER_UPDATE_SUCCESS = "Successfully updated user information"

        // Error messages
        const val INCORRECT_PASSWORD = "Your password/email is incorrect. Please try again."
        const val MISSING_FIELDS = "Please enter all fields."
        const val MISSING_USER = "User not found. Please register."
        const val PASSWORDS_DO_NOT_MATCH = "Passwords do not match."
        const val NO_INTERNET = "Network error. Please check your internet connection."

        // Shared Preference files:
        const val APP_PREFERENCES: String = "com.leewilson.movienights.APP_PREFERENCES"

        // Shared Preference keys
        const val PREVIOUS_AUTH_USER = "com.leewilson.movienights.PREVIOUS_AUTH_USER"
        const val CURRENT_USER_UID = "com.leewilson.movienights.CURRENT_USER_UID"

        // uid string extra
        const val UID_STRING_EXTRA = "com.leewilson.movienights.UID_STRING_EXTRA"
    }
}