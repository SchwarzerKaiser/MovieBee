package com.leewilson.movienights.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.movienights.repository.auth.AuthRepository
import com.leewilson.movienights.ui.auth.state.AuthStateEvent
import com.leewilson.movienights.ui.auth.state.AuthStateEvent.*
import com.leewilson.movienights.ui.auth.state.AuthViewState
import com.leewilson.movienights.util.AbsentLiveData
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class AuthViewModel @ViewModelInject constructor(
    val repository: AuthRepository
) : ViewModel() {

    private val _stateEvent: MutableLiveData<AuthStateEvent> = MutableLiveData()

    val dataState: LiveData<DataState<AuthViewState>> =
        Transformations.switchMap(_stateEvent) { event ->
            handleStateEvent(event)
        }

    private fun handleStateEvent(
        stateEvent: AuthStateEvent
    ) : LiveData<DataState<AuthViewState>> {
        when (stateEvent) {
            is ExistingUserLoginEvent -> {
                return liveData(Dispatchers.IO) {
                    emit(DataState.loading<AuthViewState>(isLoading = true))
                    val result = repository.loginUserIfExisting()
                    emit(result)
                }
            }

            is LoginEvent -> {
                return liveData(Dispatchers.IO) {
                    emit(DataState.loading(isLoading = true))
                    val result = repository.loginWithCredentials(
                        stateEvent.email,
                        stateEvent.password
                    )
                    emit(result)
                }
            }

            is RegisterEvent -> {
                return liveData(Dispatchers.IO) {
                    emit(DataState.loading(isLoading = true))
                    val result = repository.register(
                        stateEvent.name,
                        stateEvent.email,
                        stateEvent.password,
                        stateEvent.confirmPassword
                    )
                    emit(result)
                }
            }
        }
    }

    fun setStateEvent(event: AuthStateEvent) {
        _stateEvent.value = event
    }
}