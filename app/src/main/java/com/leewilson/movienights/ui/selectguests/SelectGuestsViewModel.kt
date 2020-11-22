package com.leewilson.movienights.ui.selectguests

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leewilson.movienights.model.FollowUser
import com.leewilson.movienights.repository.main.SelectGuestsRepository
import kotlinx.coroutines.launch

class SelectGuestsViewModel @ViewModelInject constructor(
    private val repository: SelectGuestsRepository
) : ViewModel() {

    val selectedUsers = ArrayList<FollowUser>()

    private val _users = MutableLiveData<List<FollowUser>>()
    val users: LiveData<List<FollowUser>>
        get() = _users

    fun fetchUids() {
        viewModelScope.launch {
            val results = repository.getFollowing()
            _users.value = results
        }
    }
}