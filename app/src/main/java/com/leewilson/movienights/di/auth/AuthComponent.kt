package com.leewilson.movienights.di.auth

import com.leewilson.movienights.ui.auth.AuthActivity
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(authActivity: AuthActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }
}