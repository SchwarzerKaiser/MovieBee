package com.leewilson.moviebee.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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

): Parcelable