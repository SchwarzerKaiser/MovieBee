package com.leewilson.movienights.model

import com.google.firebase.Timestamp

data class MovieNight(
    val hostUid: String? = null,
    val guestUids: List<String> = emptyList(),
    val dateCreated: Timestamp? = null,
    val dateOfEvent: Timestamp? = null,
    val omdbId: String? = null,
    val imageUrl: String? = null
)