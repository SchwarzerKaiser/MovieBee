package com.leewilson.moviebee.ui.main.feed.usersearch.state

sealed class UserSearchStateEvent {
    data class SearchUsers(
        val searchTerm: String
    ) : UserSearchStateEvent()
}