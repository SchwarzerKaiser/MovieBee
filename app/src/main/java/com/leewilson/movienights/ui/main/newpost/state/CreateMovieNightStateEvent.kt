package com.leewilson.movienights.ui.main.newpost.state

import com.leewilson.movienights.model.MovieNight

sealed class CreateMovieNightStateEvent {

    class LoadMovie(
        val id: String
    ) : CreateMovieNightStateEvent()

    class SaveMovieNight(
        val movieNight: MovieNight
    ) : CreateMovieNightStateEvent()
}