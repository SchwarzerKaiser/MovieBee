package com.leewilson.movienights.model


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rating(

    @SerializedName("Source")
    @Expose
    val source: String,

    @SerializedName("Value")
    @Expose
    val value: String
) : Parcelable