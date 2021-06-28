package com.leewilson.moviebee.ui.auth.state

sealed class AuthStateEvent {

    object ExistingUserLoginEvent : AuthStateEvent()

    class LoginEvent(
        var email: String? = null,
        val password: String? = null
    ) : AuthStateEvent()

    class RegisterEvent(
        val name: String? = null,
        val email: String? = null,
        val password: String? = null,
        val confirmPassword: String? = null
    ) : AuthStateEvent()
}