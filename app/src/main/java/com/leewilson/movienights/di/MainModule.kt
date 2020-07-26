package com.leewilson.movienights.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.leewilson.movienights.persistence.AppDatabase
import com.leewilson.movienights.persistence.UserPropertiesDao
import com.leewilson.movienights.util.Constants
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
class MainModule {

    @Provides
    fun providePicassoInstance(): Picasso {
        return Picasso.get()
    }

    @Provides
    fun provideFirebaseDatabaseReference(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideFirebaseStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Provides
    fun provideUserPropertiesDao(
        db: AppDatabase
    ): UserPropertiesDao {
        return db.getUserPropertiesDao()
    }

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

    @Provides
    fun provideSharedPreferencesEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}