package com.leewilson.movienights.ui.main.newpost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leewilson.movienights.R
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.ui.main.BaseMainFragment

const val MOVIE_ARG = "com.leewilson.movienights.ui.main.newpost.MOVIE_ARG"
private const val TAG = "CreateMovieNightFragment"

class CreateMovieNightFragment : BaseMainFragment(R.layout.fragment_createmovienight) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val movie = it.getParcelable<Movie>(MOVIE_ARG)
            movie?.let { thismovie ->
                Log.d(TAG, thismovie.toString())
            }
        }
    }
}