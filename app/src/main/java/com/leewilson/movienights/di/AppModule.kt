package com.leewilson.movienights.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leewilson.movienights.persistence.AppDatabase
import com.leewilson.movienights.persistence.AppDatabase.Companion.DATABASE_NAME
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideGlideRequestManager(
        @ApplicationContext context: Context
    ): RequestManager {
        return Glide.with(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabaseReference(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserPropertiesDao(
        db: AppDatabase
    ): UserPropertiesDao {
        return db.getUserPropertiesDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context
            .getSharedPreferences(
                Constants.APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}