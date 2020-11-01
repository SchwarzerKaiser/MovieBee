package com.leewilson.movienights.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("Title")
    @Expose
    val title: String,

    @SerializedName("Year")
    @Expose
    val year: String,

    @SerializedName("imdbID")
    @Expose
    val imdbId: String,

    @SerializedName("Type")
    @Expose
    val type: String,

    @SerializedName("Poster")
    @Expose
    val posterUrl: String
)