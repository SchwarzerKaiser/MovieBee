package com.leewilson.movienights.ui.main.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.leewilson.movienights.repository.main.ProfileRepository

class UpdateProfileViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
): ViewModel() {

}