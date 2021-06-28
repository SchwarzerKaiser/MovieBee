package com.leewilson.moviebee.repository.main

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.leewilson.moviebee.model.FollowUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FollowRepository"

class FollowRepository @Inject constructor(
    private val firestoreRef: FirebaseFirestore
) {

    suspend fun fetchUsers(userIds: List<String>): List<FollowUser> {
        val usersCollection = firestoreRef.collection("users")
        try {
            val documents = usersCollection.whereIn("uid", userIds)
                .get()
                .await()
                .documents
            val results = documents.map {
                FollowUser(
                    it["uid"] as String,
                    it["displayName"] as String,
                    it["imageUri"] as String?
                )
            }
            Log.d(TAG, "Got results: $results")
            return results
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Error fetching users.", e)
            return ArrayList<FollowUser>()
        }
    }
}