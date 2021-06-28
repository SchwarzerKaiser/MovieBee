package com.leewilson.moviebee.ui.main.feed.state

import com.leewilson.moviebee.model.MovieNight

sealed class FeedStateEvent {
    class LoadMovieNights(val page: Int): FeedStateEvent()
    class LikeMovieNight(val movieNight: MovieNight): FeedStateEvent()
}