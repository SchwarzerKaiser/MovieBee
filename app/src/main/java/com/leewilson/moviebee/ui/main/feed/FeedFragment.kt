package com.leewilson.moviebee.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leewilson.moviebee.R
import com.leewilson.moviebee.databinding.FragmentFeedBinding
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.ui.main.BaseMainFragment
import com.leewilson.moviebee.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FeedFragment : BaseMainFragment(R.layout.fragment_feed) {

    private companion object {
        private const val TAG = "FeedFragment"
    }

    private lateinit var binding: FragmentFeedBinding
    private val viewModel: FeedViewModel by viewModels()
    private lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    /*
    1) View movienights from friends, in a specific order (TBD)
    2) Like movienights - add to likes list for movienight entity
    3) Comment on movienights - add to comment list for movienight entity
    4) Nav to movienight detail screen
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FeedAdapter(
            object : DiffUtil.ItemCallback<MovieNight>() {
                override fun areItemsTheSame(oldItem: MovieNight, newItem: MovieNight): Boolean {
                    return oldItem == newItem
                }
                override fun areContentsTheSame(oldItem: MovieNight, newItem: MovieNight): Boolean {
                    return oldItem == newItem
                }
            }
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        feedRecyclerView.adapter = adapter
        feedRecyclerView.addItemDecoration(TopSpacingItemDecoration(16))
        feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        collectPagingData()
    }

    private fun collectPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}