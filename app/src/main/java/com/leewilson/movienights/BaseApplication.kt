package com.leewilson.movienights

import android.app.Application
import com.leewilson.movienights.di.ApplicationComponent
import com.leewilson.movienights.di.DaggerApplicationComponent
import dagger.internal.DaggerCollections

class BaseApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }
}