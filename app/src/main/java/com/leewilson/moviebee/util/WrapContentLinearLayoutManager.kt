package com.leewilson.moviebee.util

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

class WrapContentLinearLayoutManager(
    context: Context
) : LinearLayoutManager(context) {

    private val TAG = "WrapContentLinearLayout"

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "IOOBE in RecyclerView")
        }
    }
}