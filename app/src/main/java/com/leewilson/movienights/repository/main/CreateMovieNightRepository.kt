package com.leewilson.movienights.repository.main

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.movienights.api.OMDBService
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightViewState
import com.leewilson.movienights.util.DataState
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "CreateMovieNightRepo"

class CreateMovieNightRepository @Inject constructor(
    private val service: OMDBService,
    private val firebaseRef: FirebaseFirestore
) {

    suspend fun getMovieDetailById(id: String) : DataState<CreateMovieNightViewState> {
        try {
            val movieDetail = service.getMovieDetailById(id)
            return DataState.data(
                null,
                CreateMovieNightViewState.MovieLoaded(movieDetail)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Something went wrong", e)
            return DataState.error("Something went wrong")
        }
    }
}