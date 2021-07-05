package com.leewilson.moviebee.repository.main

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.moviebee.model.FollowUser
import com.leewilson.moviebee.model.FullUser
import com.leewilson.moviebee.util.Constants
import com.leewilson.moviebee.util.DataState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "UserDetail"

class UserDetailRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Named("myUid") private val myUid: String?
) {

    init {
        Log.d("TestThing", "UserID: $myUid")
    }

    suspend fun getUserById(uid: String): DataState<FullUser> {
        try {
            return firestore.collection("users")
                .document(uid)
                .get()
                .await()
                .toObject(FullUser::class.java)
                .let { DataState.data(data = it) }
        } catch (e: Exception) {
            return DataState.error("Something went wrong!")
        }
    }

    /**
     * If my user ID is in their list of followers, unfollow the user. Otherwise follow them.
     *
     * @return FullUser object once the transaction is complete.
     */
    suspend fun followOrUnfollow(uid: String): DataState<FullUser> {
        try {
            val collection = firestore.collection("users")
            val targetDocument = collection.document(uid)
            val myDocument = collection.document(myUid!!)

            val targetFollowers = targetDocument.get()
                .await()
                .toObject(FullUser::class.java)
                ?.followers

            Log.d(TAG, "Target user followers: $targetFollowers")
            Log.d(TAG, "My UID: $myUid")

            var followed = false

            if (targetFollowers == null) {
                Log.d(TAG, "No `followers` field. Follow them.")
                // No `followers` field. Follow them.
                targetDocument.update("followers", FieldValue.arrayUnion(myUid)).await()
                myDocument.update("following", FieldValue.arrayUnion(uid)).await()
                followed = true
            } else {
                if (targetFollowers.contains(myUid)) {
                    Log.d(TAG, "Already following, so unfollow.")
                    targetDocument.update("followers", FieldValue.arrayRemove(myUid)).await()
                    myDocument.update("following", FieldValue.arrayRemove(uid)).await()
                } else {
                    Log.d(TAG, "Not following, so follow them.")
                    targetDocument.update("followers", FieldValue.arrayUnion(myUid)).await()
                    myDocument.update("following", FieldValue.arrayUnion(uid)).await()
                    followed = true
                }
            }

            // Get updated target user object
            val updatedUser = targetDocument.get().await().toObject(FullUser::class.java)

            return DataState.data(
                message = if (followed) "Followed ${updatedUser?.displayName}"
                    else "Unfollowed ${updatedUser?.displayName}",
                data = updatedUser
            )
        } catch (e: Exception) {
            return DataState.error("Something went wrong.")
        }
    }

//    suspend fun follow(uid: String): DataState<FullUser> {
//
//    }
//
//    suspend fun unfollow(uid: String): DataState<FullUser> {
//
//    }
}