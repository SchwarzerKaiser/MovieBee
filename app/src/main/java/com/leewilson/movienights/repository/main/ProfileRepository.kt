package com.leewilson.movienights.repository.main

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.ui.main.profile.state.ProfileViewState
import com.leewilson.movienights.util.Constants
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val sharedPrefEditor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val userDao: UserPropertiesDao
) {

    private val TAG = "ProfileRepository"

    suspend fun getUserData(): DataState<ProfileViewState> {
        val collectionRef = firebaseFirestore.collection("users")
        val uid = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")
        try {
            val viewState = collectionRef
                .document(uid!!)
                .get()
                .await()
                .toObject(ProfileViewState::class.java)
            Log.d(TAG, "UserData: $viewState")
            return DataState.data(null, viewState)
        } catch (e: Exception) {
            Log.e(TAG, "getUserData", e)
            return DataState.error(e.message.toString())
        }
    }

    fun removeUserData() = GlobalScope.launch(Dispatchers.IO) {
        sharedPrefEditor.remove(Constants.PREVIOUS_AUTH_USER)
        sharedPrefEditor.remove(Constants.CURRENT_USER_UID)
        sharedPrefEditor.apply()
        userDao.nullifyOnLogout()
    }
}