package com.leewilson.movienights.ui.main.newpost

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.leewilson.movienights.repository.main.CreateMovieNightRepository
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightStateEvent
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightViewState
import com.leewilson.movienights.util.DataState
import kotlinx.coroutines.Dispatchers

class CreateMovieNightViewModel @ViewModelInject constructor(
    private val repository: CreateMovieNightRepository
) : ViewModel() {

    private val _stateEvent = MutableLiveData<CreateMovieNightStateEvent>()

    val dataState: LiveData<DataState<CreateMovieNightViewState>>
            = Transformations.switchMap(_stateEvent) { event ->
        handleStateEvent(event)
    }

    private fun handleStateEvent(
        event: CreateMovieNightStateEvent
    ) : LiveData<DataState<CreateMovieNightViewState>> {
        when (event) {
            is CreateMovieNightStateEvent.LoadMovie -> {
                return liveData(Dispatchers.IO) {
                    emit(DataState.loading(true))
                    val result = repository.getMovieDetailById(event.id)
                    emit(result)
                }
            }
        }
    }

    fun setStateEvent(stateEvent: CreateMovieNightStateEvent) {
        _stateEvent.value = stateEvent
    }
}