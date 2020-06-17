package com.leewilson.movienights.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.persistence.AppDatabase
import com.leewilson.movienights.persistence.AppDatabase.Companion.DATABASE_NAME
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: BaseApplication): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserPropertiesDao(db: AppDatabase): UserPropertiesDao {
        return db.getUserPropertiesDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: BaseApplication): SharedPreferences {
        return application.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}