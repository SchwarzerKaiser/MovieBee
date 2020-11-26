package com.leewilson.movienights.model

data class MovieNight(
    val hostUid: String,
    val guestUids: List<String>,
    val omdbId: String,
    val date: String,
    val timeOfDay: String
)