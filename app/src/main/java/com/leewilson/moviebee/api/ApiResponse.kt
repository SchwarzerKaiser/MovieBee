package com.leewilson.moviebee.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.leewilson.moviebee.model.Movie

data class ApiResponse(

    @SerializedName("Search")
    @Expose
    val results: List<Movie>,

    @SerializedName("totalResults")
    @Expose
    val totalResults: Int
)