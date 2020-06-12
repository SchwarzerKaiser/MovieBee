package com.leewilson.movienights.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leewilson.movienights.repository.auth.AuthRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class AuthViewModelProviderFactory @Inject constructor(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        } else
            throw IllegalArgumentException(
                "AuthViewModelProviderFactory: Invalid ViewModel: ${modelClass.simpleName}."
            )
    }
}