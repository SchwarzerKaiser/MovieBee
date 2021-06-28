package com.leewilson.moviebee.ui.main.newpost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.leewilson.moviebee.R
import com.leewilson.moviebee.model.Movie
import com.leewilson.moviebee.ui.main.BaseMainFragment
import com.leewilson.moviebee.ui.main.newpost.state.SearchStateEvent
import com.leewilson.moviebee.ui.main.newpost.state.SearchViewState
import com.leewilson.moviebee.util.TopSpacingItemDecoration
import com.leewilson.moviebee.util.WrapContentLinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_searchmovie.*

@AndroidEntryPoint
class SearchMovieFragment : BaseMainFragment(R.layout.fragment_searchmovie) {

    private val TAG = "SearchMovieFragment"

    private val viewModel: SearchMovieViewModel by viewModels()

    private var listMovies: MutableList<Movie> = ArrayList()

    private val moviesAdapter: SearchMoviesAdapter by lazy {
        SearchMoviesAdapter(object : SearchMoviesAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Movie) {
                navToCreateMovieNightFragment(item)
            }
        }).apply {
            addOnBottomReachedListener {
                Log.d(TAG, "Reached the bottom!")
                viewModel.requestMoreResults()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        subscribeObservers()
        setSearchListener()
    }

    private fun setSearchListener() {
        movieSearchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    listMovies.clear()
                    viewModel.page = 1
                    viewModel.setStateEvent(SearchStateEvent.SearchMoviesStateEvent(it))
                    hideSoftKeyboard(movieSearchField)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // n/a
                return false
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            showProgressBar(dataState.isLoading)
            when (dataState) {
                is SearchViewState.ErrorState -> {
                    showSnackbar(dataState.errorMessage)
                }
                is SearchViewState.SearchResultsState -> {
                    viewModel.resultsLoaded += dataState.listMovies.size
                    viewModel.totalResults = dataState.totalResults
                    moviesAdapter.submitList(dataState.listMovies)
                    moviesAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setUpRecyclerView() = moviesRecyclerView.apply {
        adapter = moviesAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
        addItemDecoration(
            TopSpacingItemDecoration(30)
        )
    }

    private fun navToCreateMovieNightFragment(item: Movie) {
        val args = Bundle()
        args.putParcelable(MOVIE_ARG, item)
        findNavController().navigate(R.id.action_searchMovieFragment_to_createMovieNightFragment, args)
    }

    override fun onDestroyView() {
        moviesRecyclerView.adapter = null
        super.onDestroyView()
    }
}