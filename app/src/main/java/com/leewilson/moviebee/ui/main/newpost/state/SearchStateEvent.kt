package com.leewilson.moviebee.ui.main.newpost.state

sealed class SearchStateEvent {

    data class SearchMoviesStateEvent(
        val searchTerm: String
    ) : SearchStateEvent()

    object GetNextPageRequest : SearchStateEvent()
}