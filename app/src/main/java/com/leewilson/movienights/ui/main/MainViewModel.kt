package com.leewilson.movienights.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.leewilson.movienights.repository.main.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository
): ViewModel() {

    fun logout() {
        GlobalScope.launch {
            repository.nullifyStoredUser()
        }
    }
}