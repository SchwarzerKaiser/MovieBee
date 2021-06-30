package com.leewilson.moviebee.model

import com.google.firebase.Timestamp

data class MovieNight(
    var uid: String? = null,
    var hostName: String? = null,
    val movieName: String? = null,
    val hostUid: String? = null,
    val guestUids: List<String> = emptyList(),
    val dateCreated: Timestamp? = null,
    val dateOfEvent: Timestamp? = null,
    val omdbId: String? = null,
    val imageUrl: String? = null,
    var likeUids: MutableList<String>? = null
)