package com.leewilson.movienights.di

import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.di.auth.AuthComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubcomponentsModule::class
    ]
)
interface ApplicationComponent {

    fun authComponent(): AuthComponent.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: BaseApplication): Builder

        fun build(): ApplicationComponent
    }
}