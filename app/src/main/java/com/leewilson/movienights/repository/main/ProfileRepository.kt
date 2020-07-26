package com.leewilson.movienights.repository.main

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.ui.main.profile.state.ProfileViewState
import com.leewilson.movienights.util.Constants
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalStateException
import java.net.URI
import java.net.URL
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sharedPrefEditor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val userDao: UserPropertiesDao,
    private val storageReference: StorageReference
) {

    private val TAG = "ProfileRepository"

    suspend fun getUserData(): DataState<ProfileViewState> {
        val collectionRef = firebaseFirestore.collection("users")
        val uid = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")
        return try {
            val viewState = collectionRef
                .document(uid!!)
                .get()
                .await()
                .toObject(ProfileViewState::class.java)
            DataState.data(null, viewState)
        } catch (e: FirebaseNetworkException) {
            DataState.error(e.message.toString())
        }
    }

    suspend fun updateFirestoreUser(propertiesMap: HashMap<String, Any>): DataState<ProfileViewState> {
        val collectionRef = firebaseFirestore.collection("users")
        val uid = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")
        try {

            // If empty return nothing
            if (propertiesMap.isEmpty())
                return DataState.data(null)

            // If there's an email update then action that
            if (propertiesMap.containsKey("email")) {
                val user = auth.currentUser
                user?.updateEmail(propertiesMap["email"] as String)?.await()
            }

            // If there's a new image URI then action that
            if (propertiesMap.containsKey("imageUri")) {
                val downloadUrl = updateProfileImage(Uri.parse(propertiesMap["imageUri"] as String))
                propertiesMap["imageUri"] = downloadUrl.toString()
            }

            // Update user document in Firestore
            collectionRef
                .document(uid!!)
                .set(propertiesMap, SetOptions.merge())
                .await()

        } catch (e: Exception) {
            return DataState.error(Constants.USER_UPDATE_FAILED)
        }
        return DataState.data(Constants.USER_UPDATE_SUCCESS)
    }

    fun removeUserData() = GlobalScope.launch(Dispatchers.IO) {
        sharedPrefEditor.remove(Constants.PREVIOUS_AUTH_USER)
        sharedPrefEditor.remove(Constants.CURRENT_USER_UID)
        sharedPrefEditor.apply()
        userDao.nullifyOnLogout()
    }

    suspend fun updateProfileImage(uri: Uri): Uri? {
        val uid = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")
        val userRef = storageReference.child("userImages").child(uid!!)
        try {
            val task = userRef.putFile(uri)
            var getUrlTask: Task<Uri>? = null
            task.addOnSuccessListener { snapshot ->
                getUrlTask = snapshot.storage.downloadUrl
            }.await()
            getUrlTask?.let {
                return it.await()
            }
        } catch (e: FirebaseException) {
            Log.e(TAG, "updateProfileImage: ", e)
        }
        return null
    }
}