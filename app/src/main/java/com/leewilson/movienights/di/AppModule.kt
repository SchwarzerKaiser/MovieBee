package com.leewilson.movienights.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.persistence.AppDatabase
import com.leewilson.movienights.persistence.AppDatabase.Companion.DATABASE_NAME
import com.leewilson.movienights.persistence.UserPropertiesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDatabase(application: BaseApplication): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserPropertiesDao(db: AppDatabase): UserPropertiesDao {
        return db.getUserPropertiesDao()
    }
}