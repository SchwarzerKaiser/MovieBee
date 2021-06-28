package com.leewilson.moviebee.ui.main.newpost.state

import com.leewilson.moviebee.model.MovieNight

sealed class CreateMovieNightStateEvent {

    class LoadMovie(
        val id: String
    ) : CreateMovieNightStateEvent()

    class SaveMovieNight(
        val movieNight: MovieNight
    ) : CreateMovieNightStateEvent()
}