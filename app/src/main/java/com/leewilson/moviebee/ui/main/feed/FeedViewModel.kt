package com.leewilson.moviebee.ui.main.feed

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.firestore.FirebaseFirestore
import com.leewilson.moviebee.repository.main.feed.FeedNetworkPagingSource

class FeedViewModel @ViewModelInject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val flow = Pager(PagingConfig(pageSize = 10)) {
        FeedNetworkPagingSource(firestore, sharedPreferences)
    }.flow.cachedIn(viewModelScope)
}