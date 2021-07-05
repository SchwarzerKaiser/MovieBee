package com.leewilson.moviebee.repository.main

import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.moviebee.model.FollowUser
import com.leewilson.moviebee.util.Constants
import com.leewilson.moviebee.util.DataState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserSearchRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getUsersBySearchTerm(searchTerm: String): DataState<List<FollowUser>> {
        try {
            val myId = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")

            val usersWithName = firestore.collection("users")
                .whereEqualTo("displayName", searchTerm)
                .get().await().documents.map {
                    FollowUser(
                        uid = it["uid"] as String,
                        displayName = it["displayName"] as String,
                        imageUrl = it["imageUri"] as String?
                    )
                }

            val usersWithEmail = firestore.collection("users")
                .whereEqualTo("email", searchTerm)
                .get().await().documents.map {
                    FollowUser(
                        uid = it["uid"] as String,
                        displayName = it["displayName"] as String,
                        imageUrl = it["imageUri"] as String?
                    )
                }

            val result = (usersWithName + usersWithEmail)
                .distinctBy { it.uid }
                .filter { it.uid != myId }

            return if (result.isEmpty()) {
                DataState.error("No users found")
            } else {
                DataState.data(data = result)
            }
        } catch (e: Exception) {
            return DataState.error("Sorry! Something went wrong.")
        }
    }
}