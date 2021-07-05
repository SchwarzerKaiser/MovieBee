package com.leewilson.moviebee.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leewilson.moviebee.api.OMDBService
import com.leewilson.moviebee.persistence.AppDatabase
import com.leewilson.moviebee.persistence.UserPropertiesDao
import com.leewilson.moviebee.util.Constants
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
class MainModule {

    @Provides
    @Named("myUid")
    fun provideUserUid(
        sharedPreferences: SharedPreferences
    ): String? {
        return sharedPreferences.getString(
            Constants.CURRENT_USER_UID, null
        )
    }

    @Provides
    fun provideGsonInstance(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideRetrofitInstance(gsonConverterFactory: GsonConverterFactory): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(Constants.OMDB_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideOmdbService(retrofit: Retrofit): OMDBService {
        return retrofit.create(OMDBService::class.java)
    }

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

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}