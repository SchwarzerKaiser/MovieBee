package com.leewilson.movienights.ui.main.newpost.state

import com.leewilson.movienights.model.MovieDetail
import com.leewilson.movienights.util.ConsumableEvent

sealed class CreateMovieNightViewState {

    class MovieLoaded(
        val details: MovieDetail
    ) : CreateMovieNightViewState()
}