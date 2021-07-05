package com.leewilson.moviebee.ui.main.feed.usersearch.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.moviebee.model.FollowUser
import com.leewilson.moviebee.repository.main.UserSearchRepository
import com.leewilson.moviebee.ui.main.feed.usersearch.state.UserSearchStateEvent
import com.leewilson.moviebee.util.DataState

class UserSearchViewModel @ViewModelInject constructor(
    private val repository: UserSearchRepository
) : ViewModel() {

    private val _stateEvent: MutableLiveData<UserSearchStateEvent> = MutableLiveData()

    val dataState: LiveData<DataState<List<FollowUser>>> =
        Transformations.switchMap(_stateEvent) { event ->
            handleStateEvent(event)
        }

    private fun handleStateEvent(stateEvent: UserSearchStateEvent): LiveData<DataState<List<FollowUser>>> {
        when (stateEvent) {
            is UserSearchStateEvent.SearchUsers -> {
                return liveData(viewModelScope.coroutineContext) {
                    emit(DataState.loading(true))
                    val result = repository.getUsersBySearchTerm(stateEvent.searchTerm)
                    emit(result)
                }
            }
        }
    }

    fun setStateEvent(event: UserSearchStateEvent) {
        _stateEvent.value = event
    }
}