package com.leewilson.movienights.repository.main

import android.util.Log
import com.leewilson.movienights.api.OMDBService
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.ui.main.newpost.state.SearchViewState
import javax.inject.Inject

class SearchMovieRepository @Inject constructor(
    private val service: OMDBService
) {

    private val TAG = "SearchMovieRepository"

    suspend fun searchMovies(query: String, page: Int): SearchViewState {
        try {
            val response = service.searchOmdbByName(query, page)
            if (response.results.isNullOrEmpty()) {
                return SearchViewState.ErrorState(
                    "No results found."
                )
            }
            return SearchViewState.SearchResultsState(
                response.results as MutableList<Movie>,
                response.totalResults
            )
        } catch (t: Throwable) {
            Log.e(TAG, "Page: $page, Exception: $t")
            return SearchViewState.ErrorState(
                "Oops! Something went wrong."
            )
        }
    }
}