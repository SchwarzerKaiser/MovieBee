package com.leewilson.moviebee.ui.main.newpost.state

import com.leewilson.moviebee.model.Movie

sealed class SearchViewState(val isLoading: Boolean) {

    object LoadingState : SearchViewState(true)

    data class SearchResultsState(
        var listMovies: MutableList<Movie>,
        val totalResults: Int
    ) : SearchViewState(false)

    data class ErrorState(
        val errorMessage: String
    ) : SearchViewState(false)
}