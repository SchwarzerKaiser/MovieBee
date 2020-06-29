package com.leewilson.movienights.repository.main

import android.content.SharedPreferences
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.util.Constants
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val sharedPreferencesEditor: SharedPreferences.Editor,
    private val userPropertiesDao: UserPropertiesDao
) {

    fun nullifyStoredUser() {
        userPropertiesDao.nullifyOnLogout()
        sharedPreferencesEditor.remove(Constants.PREVIOUS_AUTH_USER)
        sharedPreferencesEditor.apply()
    }
}