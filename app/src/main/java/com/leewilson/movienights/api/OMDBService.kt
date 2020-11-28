package com.leewilson.movienights.api

import com.leewilson.movienights.BuildConfig
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.model.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBService {

    @GET("?apikey=${BuildConfig.OMDB_KEY}")
    suspend fun searchOmdbByName(
        @Query("s") searchTerm: String,
        @Query("page") page: Int
    ): ApiResponse

    @GET("?apikey=${BuildConfig.OMDB_KEY}")
    suspend fun getMovieDetailById(
        @Query("i") id: String
    ): MovieDetail
}