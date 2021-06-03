package com.leewilson.movienights.repository.main

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.leewilson.movienights.R
import com.leewilson.movienights.api.OMDBService
import com.leewilson.movienights.model.MovieNight
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightViewState
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "CreateMNRepo"

class CreateMovieNightRepository @Inject constructor(
    private val service: OMDBService,
    private val firebaseRef: FirebaseFirestore,
    private val context: Context
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