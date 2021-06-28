package com.leewilson.moviebee.model


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetail(

    @SerializedName("Actors")
    @Expose
    val actors: String? = null,

    @SerializedName("Awards")
    @Expose
    val awards: String? = null,

    @SerializedName("BoxOffice")
    @Expose
    val boxOffice: String? = null,

    @SerializedName("Country")
    @Expose
    val country: String? = null,

    @SerializedName("DVD")
    @Expose
    val dvd: String? = null,

    @SerializedName("Director")
    @Expose
    val director: String? = null,

    @SerializedName("Genre")
    @Expose
    val genre: String? = null,

    @SerializedName("imdbID")
    @Expose
    val imdbID: String,

    @SerializedName("imdbRating")
    @Expose
    val imdbRating: String? = null,

    @SerializedName("imdbVotes")
    @Expose
    val imdbVotes: String? = null,

    @SerializedName("Language")
    @Expose
    val language: String? = null,

    @SerializedName("Metascore")
    @Expose
    val metascore: String? = null,

    @SerializedName("Plot")
    @Expose
    val plot: String? = null,

    @SerializedName("Poster")
    @Expose
    val poster: String? = null,

    @SerializedName("Production")
    @Expose
    val production: String? = null,

    @SerializedName("Rated")
    @Expose
    val rated: String? = null,

    @SerializedName("Ratings")
    @Expose
    val ratings: List<Rating>? = null,

    @SerializedName("Released")
    @Expose
    val released: String? = null,

    @SerializedName("Response")
    @Expose
    val response: String? = null,

    @SerializedName("Runtime")
    @Expose
    val runtime: String? = null,

    @SerializedName("Title")
    @Expose
    val title: String? = null,

    @SerializedName("Type")
    @Expose
    val type: String? = null,

    @SerializedName("Website")
    @Expose
    val website: String? = null,

    @SerializedName("Writer")
    @Expose
    val writer: String? = null,

    @SerializedName("Year")
    @Expose
    val year: String? = null
) : Parcelable