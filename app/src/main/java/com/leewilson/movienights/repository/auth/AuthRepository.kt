package com.leewilson.movienights.repository.auth

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.movienights.model.UserProperties
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.model.User
import com.leewilson.movienights.ui.auth.state.AuthViewState
import com.leewilson.movienights.util.Constants
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDbRef: FirebaseFirestore,
    private val authDao: UserPropertiesDao,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
) {

    private val TAG = "AuthRepository"

    suspend fun loginUserIfExisting(): DataState<AuthViewState> {

        val email = sharedPreferences.getString(Constants.PREVIOUS_AUTH_USER, null)
            ?: return DataState.data<AuthViewState>(null, AuthViewState(
                false,
                null
            ))

        val userProperties = authDao.searchByEmail(email)
        // This shouldn't ever happen, unless there's some database config error
            ?: return DataState.error<AuthViewState>("Database error! Please reinstall.")

        return login(email, userProperties.password)
    }

    suspend fun loginWithCredentials(email: String?, password: String?): DataState<AuthViewState> {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
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

            storeUserLocally(authResult.user!!, password)

            return DataState.data(
                data = AuthViewState(
                    true,
                    authResult.user?.uid
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "login: FirebaseAuthInvalidUserException: ", e)
            return DataState.error(
                Constants.MISSING_USER
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "login: FirebaseAuthInvalidCredentialsException: ", e)
            return DataState.error(
                Constants.INCORRECT_PASSWORD
            )
        } catch (e: FirebaseNetworkException) {
            Log.e(TAG, "login: FirebaseNetworkException", e)
            return DataState.error(
                Constants.NO_INTERNET
            )
        }
    }

    suspend fun register(
        name: String?, email: String?,
        password: String?, confirmPassword: String?
    ): DataState<AuthViewState> {

        if (name.isNullOrBlank() ||
            email.isNullOrBlank() ||
            password.isNullOrBlank() ||
            confirmPassword.isNullOrBlank()
        )
            return DataState.error(Constants.MISSING_FIELDS)

        if (password != confirmPassword)
            return DataState.error(Constants.PASSWORDS_DO_NOT_MATCH)

        try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            authResult.user?.let { user ->
                storeUserLocally(user, password)
                createDbUser(name, user, email)
                return@register DataState.data(
                    null,
                    AuthViewState(
                        true,
                        user.uid
                    )
                )
            }
        } catch (e: FirebaseAuthWeakPasswordException) {
            return DataState.error(e.reason.toString())
        } catch (e: FirebaseAuthUserCollisionException) {
            return DataState.error(e.message.toString())
        }

        // user is null. unknown error
        return DataState.error("Something went wrong.")
    }

    private fun createDbUser(name: String, user: FirebaseUser, email: String) {
        firebaseDbRef.collection("users")
            .document(user.uid)
            .set(hashMapOf(
                "displayName" to name,
                "email" to email,
                "uid" to user.uid
            ))
    }

    private fun storeUserLocally(user: FirebaseUser, password: String) {
        sharedPreferencesEditor.putString(Constants.PREVIOUS_AUTH_USER, user.email)
        sharedPreferencesEditor.putString(Constants.CURRENT_USER_UID, user.uid)
        sharedPreferencesEditor.apply()
        authDao.insertAndReplace(
            UserProperties(
                0,
                user.email!!,
                password
            )
        )
    }
}
