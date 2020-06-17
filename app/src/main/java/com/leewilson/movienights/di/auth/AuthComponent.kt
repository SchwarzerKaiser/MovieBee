package com.leewilson.movienights.di.auth

import com.leewilson.movienights.ui.auth.AuthActivity
import com.leewilson.movienights.ui.auth.LoginFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(authActivity: AuthActivity)

    fun inject(loginFragment: LoginFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): AuthComponent
    }
}