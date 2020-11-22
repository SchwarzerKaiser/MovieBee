package com.leewilson.movienights.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FollowUser(
    val uid: String,
    val displayName: String,
    val imageUrl: String
) : Parcelable