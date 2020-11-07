package com.leewilson.movienights.ui.follow

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leewilson.movienights.model.FollowUser
import com.leewilson.movienights.repository.main.FollowRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FollowViewModel @ViewModelInject constructor(
    private val repository: FollowRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<FollowUser>>()
    val users: LiveData<List<FollowUser>>
        get() = _users

    fun fetchUsers(userIds: List<String>) {
        viewModelScope.launch {
            val results = repository.fetchUsers(userIds)
            _users.value = results
        }
    }
}