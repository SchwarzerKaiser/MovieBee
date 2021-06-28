package com.leewilson.moviebee.ui.main.newpost.state

import com.leewilson.moviebee.model.MovieDetail

sealed class CreateMovieNightViewState {

    class MovieLoaded(
        val details: MovieDetail
    ) : CreateMovieNightViewState()

    class MovieNightSaved(
        val success: Boolean
    ) : CreateMovieNightViewState()
}