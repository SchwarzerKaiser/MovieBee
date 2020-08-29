package com.leewilson.movienights.ui.main.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.movienights.repository.main.ProfileRepository
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent.UpdateUserData
import com.leewilson.movienights.ui.main.profile.state.ProfileViewState
import com.leewilson.movienights.util.AbsentLiveData
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.yield

class ProfileViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _stateEvent: MutableLiveData<ProfileStateEvent> = MutableLiveData()
    private var cachedFields: ProfileViewState? = null

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
                    cachedFields = result.data?.peekContent()
                    emit(result)
                }
            }

            is UpdateUserData -> {
                return liveData {
                    emit(DataState.loading(true))
                    val updatesMap = mapUpdatedValues(stateEvent)
                    val result = repository.updateFirestoreUser(updatesMap)
                    emit(result)
                }
            }
        }
    }

    fun removeUserData() {
        repository.removeUserData()
    }

    fun setStateEvent(event: ProfileStateEvent) {
        _stateEvent.value = event
    }

    private fun mapUpdatedValues(stateEvent: UpdateUserData): HashMap<String, String?> {
        cachedFields?.let { cachedFields ->
            val updates = HashMap<String, String?>()
            if (cachedFields.displayName != stateEvent.displayName)
                updates["displayName"] = stateEvent.displayName
            if (cachedFields.bio != stateEvent.bio)
                updates["bio"] = stateEvent.bio
            if (cachedFields.email != stateEvent.email)
                updates["email"] = stateEvent.email
            if (cachedFields.imageUri != stateEvent.imageUri)
                stateEvent.imageUri?.let { updates["imageUri"] = it }
            return updates
        } ?: return HashMap()
    }
}