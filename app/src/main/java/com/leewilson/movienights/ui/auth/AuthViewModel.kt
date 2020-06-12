package com.leewilson.movienights.ui.auth

import androidx.lifecycle.ViewModel
import com.leewilson.movienights.repository.auth.AuthRepository

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {


}