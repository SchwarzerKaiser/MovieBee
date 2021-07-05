package com.leewilson.moviebee.ui.main.feed.usersearch.state

sealed class UserDetailStateEvent {
    data class GetUserDetail(val uid: String) : UserDetailStateEvent()
    data class FollowButton(val uid: String) : UserDetailStateEvent()
}