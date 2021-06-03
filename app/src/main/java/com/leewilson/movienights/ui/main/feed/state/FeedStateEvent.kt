package com.leewilson.movienights.ui.main.feed.state

import com.leewilson.movienights.model.MovieNight

sealed class FeedStateEvent {
    class LoadMovieNights(val page: Int): FeedStateEvent()
    class LikeMovieNight(val movieNight: MovieNight): FeedStateEvent()
}