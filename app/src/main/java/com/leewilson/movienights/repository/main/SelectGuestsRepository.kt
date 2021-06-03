package com.leewilson.movienights.repository.main

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.leewilson.movienights.model.FollowUser
import com.leewilson.movienights.model.User
import com.leewilson.movienights.util.Constants.Companion.CURRENT_USER_UID
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

class SelectGuestsRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TAG = "SelectGuestsRepository"
    }

    suspend fun getFollowing() : List<FollowUser> {
        try {
            val users = firebaseFirestore.collection("users")
            val currentUserUid = sharedPreferences.getString(CURRENT_USER_UID, "")
            if (currentUserUid.isNullOrBlank()) throw IllegalStateException()
            val uids = users.document(currentUserUid)
                .get().await().get("following") as List<String>
            Log.d(TAG, "getFollowing: $uids")

            return users.whereIn("uid", uids)
                .get().await().documents.map {
                    FollowUser(
                        it["uid"] as String,
                        it["displayName"] as String,
                        it["imageUri"] as String?
                    )
                }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Shared preference key was not correct. Please reinstall.", e)
            return emptyList()
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Error contacting the server", e)
            return emptyList()
        }
    }
}