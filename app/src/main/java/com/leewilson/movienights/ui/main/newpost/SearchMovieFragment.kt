package com.leewilson.movienights.ui.main.newpost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leewilson.movienights.R
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.newpost.state.SearchStateEvent
import com.leewilson.movienights.ui.main.newpost.state.SearchViewState
import com.leewilson.movienights.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_searchmovie.*

@AndroidEntryPoint
class SearchMovieFragment : BaseMainFragment(R.layout.fragment_searchmovie) {

    private val TAG = "SearchMovieFragment"

    private val viewModel: SearchMovieViewModel by viewModels()

    private val moviesAdapter: SearchMoviesAdapter by lazy {
        SearchMoviesAdapter(object : SearchMoviesAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Movie) {
                showSnackbar("Item selected at position: $position")
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
                    viewModel.page = 1
                    viewModel.setStateEvent(SearchStateEvent.SearchMoviesStateEvent(it))
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
                }
            }
        })
    }

    private fun setUpRecyclerView() = moviesRecyclerView.apply {
        adapter = moviesAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(
            TopSpacingItemDecoration(30)
        )
    }

    override fun onDestroyView() {
        moviesRecyclerView.adapter = null
        super.onDestroyView()
    }
}