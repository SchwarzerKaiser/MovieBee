package com.leewilson.movienights.repository.main

import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.util.Constants
import javax.inject.Inject

class MainRepository @Inject constructor(
//    val movieNightsDao: MovieNightsDao,
    val sharedPreferencesEditor: SharedPreferences.Editor,
    val userPropertiesDao: UserPropertiesDao
) {

    suspend fun nullifyStoredUser() {
        userPropertiesDao.nullifyOnLogout()
        sharedPreferencesEditor.remove(Constants.PREVIOUS_AUTH_USER)
        sharedPreferencesEditor.apply()
    }
}