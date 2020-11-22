package com.leewilson.movienights.ui.selectguests

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leewilson.movienights.R
import com.leewilson.movienights.model.FollowUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_select_guests.*

@AndroidEntryPoint
class SelectGuestsActivity : AppCompatActivity() {

    private val viewModel: SelectGuestsViewModel by viewModels()

    private val listAdapter: SelectGuestsListAdapter by lazy {
        SelectGuestsListAdapter(
            object : SelectGuestsListAdapter.Interaction {
                override fun onSelected(user: FollowUser) {
                    viewModel.selectedUsers += user
                }

                override fun onDeselected(user: FollowUser) {
                    viewModel.selectedUsers -= user
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_guests)

        subscribeObservers()
        selectGuestsRecyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@SelectGuestsActivity)
        }
        viewModel.fetchUids()
    }

    private fun subscribeObservers() {
        viewModel.users.observe(this, Observer {
            listAdapter.submitList(it)
        })
    }
}