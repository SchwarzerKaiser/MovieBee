package com.leewilson.movienights.model


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetail(

    @SerializedName("Actors")
    @Expose
    val actors: String,

    @SerializedName("Awards")
    @Expose
    val awards: String,

    @SerializedName("BoxOffice")
    @Expose
    val boxOffice: String,

    @SerializedName("Country")
    @Expose
    val country: String,

    @SerializedName("DVD")
    @Expose
    val dVD: String,

    @SerializedName("Director")
    @Expose
    val director: String,

    @SerializedName("Genre")
    @Expose
    val genre: String,

    @SerializedName("imdbID")
    @Expose
    val imdbID: String,

    @SerializedName("imdbRating")
    @Expose
    val imdbRating: String,

    @SerializedName("imdbVotes")
    @Expose
    val imdbVotes: String,

    @SerializedName("Language")
    @Expose
    val language: String,

    @SerializedName("Metascore")
    @Expose
    val metascore: String,

    @SerializedName("Plot")
    @Expose
    val plot: String,

    @SerializedName("Poster")
    @Expose
    val poster: String,

    @SerializedName("Production")
    @Expose
    val production: String,

    @SerializedName("Rated")
    @Expose
    val rated: String,

    @SerializedName("Ratings")
    @Expose
    val ratings: List<Rating>,

    @SerializedName("Released")
    @Expose
    val released: String,

    @SerializedName("Response")
    @Expose
    val response: String,

    @SerializedName("Runtime")
    @Expose
    val runtime: String,

    @SerializedName("Title")
    @Expose
    val title: String,

    @SerializedName("Type")
    @Expose
    val type: String,

    @SerializedName("Website")
    @Expose
    val website: String,

    @SerializedName("Writer")
    @Expose
    val writer: String,

    @SerializedName("Year")
    @Expose
    val year: String
) : Parcelable