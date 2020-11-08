package com.leewilson.movienights.ui.main.newpost.state

sealed class CreateMovieNightStateEvent {

    class LoadMovie(
        val id: String
    ): CreateMovieNightStateEvent()
}