package com.leewilson.moviebee.ui.main.feed

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.repository.main.feed.FeedNetworkPagingSource
import com.leewilson.moviebee.util.Constants
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val flow = Pager(PagingConfig(pageSize = 10)) {
        FeedNetworkPagingSource(firestore, sharedPreferences)
    }.flow.cachedIn(viewModelScope)

    fun like(movieNight: MovieNight) = viewModelScope.launch {
        movieNight.uid?.let { id ->
            val userId = sharedPreferences.getString(Constants.CURRENT_USER_UID, null)
            userId?.let { userId ->
                val docRef = firestore.collection("movienights").document(id)
                docRef.update("likeUids", FieldValue.arrayUnion(userId))
            }
        }
    }

    fun unlike(movieNight: MovieNight) = viewModelScope.launch {
        movieNight.uid?.let { id ->
            val userId = sharedPreferences.getString(Constants.CURRENT_USER_UID, null)
            userId?.let { userId ->
                val docRef = firestore.collection("movienights").document(id)
                docRef.update("likeUids", FieldValue.arrayRemove(userId))
            }
        }
    }
}