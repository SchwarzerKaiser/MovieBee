package com.leewilson.movienights.util

class Constants {

    companion object{

        // Error messages
        const val INCORRECT_PASSWORD = "Your password/email is incorrect. Please try again."
        const val MISSING_FIELDS = "Please enter all fields."
        const val MISSING_USER = "User not found. Please register."
        const val PASSWORDS_DO_NOT_MATCH = "Passwords do not match."

        // Shared Preference files:
        const val APP_PREFERENCES: String = "com.leewilson.movienights.APP_PREFERENCES"

        // Shared Preference keys
        const val PREVIOUS_AUTH_USER: String = "com.leewilson.movienights.PREVIOUS_AUTH_USER"

        // uid string extra
        const val UID_STRING_EXTRA = "com.leewilson.movienights.UID_STRING_EXTRA"
    }
}