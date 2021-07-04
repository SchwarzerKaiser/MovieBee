package com.leewilson.moviebee.ui.main.feed.usersearch.state

import com.leewilson.moviebee.model.FollowUser

data class UserSearchViewState(
    val usersFound: List<FollowUser>? = null,
    val errorMessage: String? = null
)