package com.leewilson.movienights.repository.auth

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.ui.auth.state.AuthViewState
import com.leewilson.movienights.util.AbsentLiveData
import com.leewilson.movienights.util.Constants
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authDao: UserPropertiesDao,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
) {

    private val TAG = "AuthRepository"

    suspend fun loginUserIfExisting(): DataState<AuthViewState> {
        val email = sharedPreferences.getString(Constants.PREVIOUS_AUTH_USER, null)
            // User not in SharedPref, so return null (will not be reacted to)
            ?: return DataState.data<AuthViewState>(null)
        val userProperties = authDao.searchByEmail(email)
            // This shouldn't ever happen, unless there's some database config error
            ?: return DataState.error<AuthViewState>("Database error! Please reinstall.")

        return login(email, userProperties.password)
    }

    suspend fun loginWithCredentials(email: String?, password: String?): DataState<AuthViewState> {
        if(email.isNullOrBlank() || password.isNullOrBlank()) {
            return DataState.error<AuthViewState>(
                Constants.MISSING_FIELDS
            )
        }
        return login(email, password)
    }

    private suspend fun login(email: String, password: String): DataState<AuthViewState> {
        try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            return DataState.data<AuthViewState>(
                data = AuthViewState(
                    authResult.user?.uid
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "loginUserIfExisting: FirebaseAuthInvalidUserException: ", e)
            return DataState.error<AuthViewState>(
                Constants.MISSING_USER
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "loginUserIfExisting: FirebaseAuthInvalidCredentialsException: ", e)
            return DataState.error<AuthViewState>(
                Constants.INCORRECT_PASSWORD
            )
        }
    }
}
