package com.leewilson.moviebee.repository.main

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.leewilson.moviebee.R
import com.leewilson.moviebee.api.OMDBService
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.ui.main.newpost.state.CreateMovieNightViewState
import com.leewilson.moviebee.util.Constants
import com.leewilson.moviebee.util.DataState
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "CreateMNRepo"

class CreateMovieNightRepository @Inject constructor(
    private val service: OMDBService,
    private val firebaseRef: FirebaseFirestore,
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getMovieDetailById(id: String) : DataState<CreateMovieNightViewState> {
        try {
            val movieDetail = service.getMovieDetailById(id)
            return DataState.data(
                null,
                CreateMovieNightViewState.MovieLoaded(movieDetail)
            )
        } catch (e: Exception) {
            Log.e(TAG, "getMovieDetailById: Something went wrong", e)
            return DataState.error(context.getString(R.string.create_movie_night_error))
        }
    }

    suspend fun saveMovieNight(movieNight: MovieNight): DataState<CreateMovieNightViewState> {
        try {
            // get cached uid
            val userId = sharedPreferences.getString(Constants.CURRENT_USER_UID, "")!!

            // first get the host's name
            val hostName = firebaseRef.collection("users")
                .document(userId)
                .get().await().get("displayName") as String
            movieNight.hostName = hostName

            firebaseRef.collection("movienights")
                .add(movieNight)
                .await()
            return DataState.data(
                context.getString(R.string.snackbar_movienight_saved),
                null
            )
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "FireStore error: ", e)
            return DataState.error(context.getString(R.string.create_movie_night_error))
        }
    }
}