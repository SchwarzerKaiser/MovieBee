package com.leewilson.moviebee.repository.main.feed

import android.content.SharedPreferences
import androidx.paging.PagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.ui.main.profile.state.ProfileViewState
import com.leewilson.moviebee.util.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

private const val ITEM_LOAD_LIMIT: Long = 5

class FeedNetworkPagingSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) : PagingSource<QuerySnapshot, MovieNight>() {

    private val userId: String by lazy {
        sharedPreferences.getString(Constants.CURRENT_USER_UID, "") ?: ""
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, MovieNight> {
        try {
            val users: ArrayList<String>? = firestore.collection("users")
                .document(userId)
                .get().await()
                .toObject(ProfileViewState::class.java)
                ?.following
            users?.add(userId)

            if (users == null || users.size == 1) {
                throw Exception("You are not following anyone.")
            }

            val orderedCollection = firestore.collection("movienights")
                .whereIn("hostUid", users)
                .orderBy("dateCreated", Query.Direction.DESCENDING)

            val currentPage = params.key ?:
                orderedCollection
                    .limit(ITEM_LOAD_LIMIT)
                    .get()
                    .await()
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = orderedCollection
                .limit(ITEM_LOAD_LIMIT)
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            return LoadResult.Page(
                data = currentPage.toObjects(MovieNight::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}