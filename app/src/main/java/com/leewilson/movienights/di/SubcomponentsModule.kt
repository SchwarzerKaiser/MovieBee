package com.leewilson.movienights.di

import com.leewilson.movienights.di.auth.AuthComponent
import dagger.Module
import javax.inject.Singleton

@Module(
    subcomponents = [
        AuthComponent::class
    ]
)
class SubcomponentsModule