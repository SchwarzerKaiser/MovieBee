package com.leewilson.moviebee.ui.main.feed.usersearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leewilson.moviebee.R
import com.leewilson.moviebee.databinding.FragmentFeedBinding
import com.leewilson.moviebee.databinding.FragmentUserSearchBinding
import com.leewilson.moviebee.model.FollowUser
import com.leewilson.moviebee.ui.main.BaseMainFragment
import com.leewilson.moviebee.ui.main.feed.usersearch.state.UserSearchStateEvent
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "UserSearch"

@AndroidEntryPoint
class UserSearchFragment : BaseMainFragment(R.layout.fragment_user_search) {

    private val viewModel: UserSearchViewModel by viewModels()

    private var _binding: FragmentUserSearchBinding? = null
    private val binding: FragmentUserSearchBinding get() = _binding!!

    private var _adapter: UserSearchAdapter? = null
    private val adapter: UserSearchAdapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setListeners()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            showProgressBar(dataState.loading)
            dataState.message?.let { consumableMsg ->
                consumableMsg.getContentIfNotHandled()?.let { msg ->
                    showSnackbar(msg)
                }
            }
            dataState.data?.let { consumableEvent ->
                val results = consumableEvent.peekContent()
                adapter.submitList(results)
                Log.d(TAG, "Found users: $results")
            }
        })
    }

    private fun setupRecyclerView() {
        binding.userSearchList.layoutManager = LinearLayoutManager(requireContext())
        _adapter = UserSearchAdapter(
            object : UserSearchAdapter.Interaction {
                override fun onUserSelected(user: FollowUser, position: Int) {
                    // Navigate to user detail
                }
            }
        )
        binding.userSearchList.adapter = adapter
    }

    private fun setListeners() {
        binding.userSearchField.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "onQueryTextSubmit")
                    query?.let { searchTerm ->
                        viewModel.setStateEvent(UserSearchStateEvent.SearchUsers(searchTerm))
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )
    }

    override fun onDestroy() {
        _binding = null
        _adapter = null
        super.onDestroy()
    }
}