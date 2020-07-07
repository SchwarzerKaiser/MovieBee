package com.leewilson.movienights.ui.main.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.movienights.repository.main.ProfileRepository
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import com.leewilson.movienights.ui.main.profile.state.ProfileViewState
import com.leewilson.movienights.util.AbsentLiveData
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.handleCoroutineException

class ProfileViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _stateEvent: MutableLiveData<ProfileStateEvent> = MutableLiveData()

    val dataState: LiveData<DataState<ProfileViewState>>
        = Transformations.switchMap(_stateEvent) { stateEvent ->
        handleStateEvent(stateEvent)
    }

    private fun handleStateEvent(
        stateEvent: ProfileStateEvent
    ): LiveData<DataState<ProfileViewState>> {
        when(stateEvent) {
            is ProfileStateEvent.FetchUserData -> {
                return liveData {
                    emit(DataState.loading(true))
                    val result = repository.getUserData()
                    emit(result)
                }
            }

            else -> return AbsentLiveData.create()
        }
    }

    fun removeUserData() {
        repository.removeUserData()
    }

    fun setStateEvent(event: ProfileStateEvent) {
        _stateEvent.value = event
    }
}