package com.leewilson.movienights.ui.main.newpost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.leewilson.movienights.R
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.model.MovieDetail
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightStateEvent
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightViewState
import dagger.hilt.android.AndroidEntryPoint

const val MOVIE_ARG = "com.leewilson.movienights.ui.main.newpost.MOVIE_ARG"
private const val TAG = "MovieNightFragment"

@AndroidEntryPoint
class CreateMovieNightFragment : BaseMainFragment(R.layout.fragment_createmovienight) {

    private val viewModel: CreateMovieNightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val movie = it.getParcelable<Movie>(MOVIE_ARG)
            movie?.let { thismovie ->
                viewModel.setStateEvent(
                    CreateMovieNightStateEvent.LoadMovie(
                        thismovie.imdbId
                    )
                )
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            showProgressBar(dataState.loading)
            dataState.message?.let { consumableEvent ->
                consumableEvent.getContentIfNotHandled()?.let {
                    showSnackbar(it)
                }
            }
            dataState.data?.let { consumableEvent ->
                val state = consumableEvent.peekContent()
                when (state) {
                    is CreateMovieNightViewState.MovieLoaded -> {
                        setMovieData(state.details)
                    }
                }
            }
        })
    }

    private fun setMovieData(details: MovieDetail) {

    }
}