package com.leewilson.movienights.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.leewilson.movienights.model.Movie

data class ApiResponse(

    @SerializedName("Search")
    @Expose
    val results: List<Movie>,

    @SerializedName("totalResults")
    @Expose
    val totalResults: Int
)