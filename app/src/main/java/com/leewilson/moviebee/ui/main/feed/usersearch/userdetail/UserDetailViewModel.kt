package com.leewilson.moviebee.ui.main.feed.usersearch.userdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.moviebee.model.FollowUser
import com.leewilson.moviebee.model.FullUser
import com.leewilson.moviebee.repository.main.UserDetailRepository
import com.leewilson.moviebee.ui.main.feed.usersearch.state.UserDetailStateEvent
import com.leewilson.moviebee.ui.main.feed.usersearch.state.UserSearchStateEvent
import com.leewilson.moviebee.util.AbsentLiveData
import com.leewilson.moviebee.util.DataState

class UserDetailViewModel @ViewModelInject constructor(
    private val repository: UserDetailRepository
) : ViewModel() {

    private val _stateEvent: MutableLiveData<UserDetailStateEvent> = MutableLiveData()

    val dataState: LiveData<DataState<FullUser>> =
        Transformations.switchMap(_stateEvent) { event ->
            handleStateEvent(event)
        }

    private fun handleStateEvent(stateEvent: UserDetailStateEvent): LiveData<DataState<FullUser>> {
        when (stateEvent) {
            is UserDetailStateEvent.GetUserDetail -> {
                return liveData(viewModelScope.coroutineContext) {
                    emit(DataState.loading(true))
                    emit(repository.getUserById(stateEvent.uid))
                }
            }
            is UserDetailStateEvent.FollowButton -> {
                return liveData(viewModelScope.coroutineContext) {
                    emit(DataState.loading(true))
                    emit(repository.followOrUnfollow(stateEvent.uid))
                }
            }
        }
    }

    fun setStateEvent(event: UserDetailStateEvent) {
        _stateEvent.value = event
    }
}