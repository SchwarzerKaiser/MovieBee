package com.leewilson.moviebee.repository.main.feed

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.ui.main.feed.state.FeedViewState
import com.leewilson.moviebee.ui.main.profile.state.ProfileViewState
import com.leewilson.moviebee.util.Constants
import com.leewilson.moviebee.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firestore: FirebaseFirestore
) {

    private val TAG = "FeedRepository"

    suspend fun getMovieNightsForUser(): Flow<DataState<FeedViewState>> = flow {
        try {
            val userId = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")
            if (!userId.isNullOrBlank()) {
                val userData = firestore
                    .collection("users")
                    .document(userId)
                    .get().await()
                    .toObject(ProfileViewState::class.java)
                userData?.following?.let { following ->
                    following.add(userId) // We want to see our movienights too.
                    val results = firestore.collection("movienights")
                        .whereIn("hostUid", following)
                        .get().await().toObjects(MovieNight::class.java)
                    emit(DataState.data<FeedViewState>(
                        null,
                        FeedViewState(results)
                    ))
                } ?: run {
                    emit(DataState.error("You are not following any users."))
                }
            } else {
                Log.wtf(TAG, "User ID is blank or null. Try and reinstall.")
                emit(DataState.error("An unknown error occurred."))
            }
        } catch (e: Exception) {
            emit(DataState.error(e.message.toString()))
        }
    }
}