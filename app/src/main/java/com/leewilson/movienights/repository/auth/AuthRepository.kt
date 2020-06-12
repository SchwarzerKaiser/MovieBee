package com.leewilson.movienights.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.leewilson.movienights.persistence.UserPropertiesDao
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val dao: UserPropertiesDao
) {

}