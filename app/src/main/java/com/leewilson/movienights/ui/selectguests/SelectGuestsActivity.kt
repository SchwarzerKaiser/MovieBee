package com.leewilson.movienights.ui.selectguests

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
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

    companion object {
        const val EXTRA_SELECTED_GUESTS = "com.leewilson.movienights.ui.selectguests.EXTRA_SELECTED_GUESTS"
    }

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
        selectGuestsProgressBar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_select_movienight_guests, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuitem_selectguests_done) {
            if (viewModel.selectedUsers.isEmpty()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putParcelableArrayListExtra(
                            EXTRA_SELECTED_GUESTS,
                            viewModel.selectedUsers
                        )
                    }
                )
            }
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeObservers() {
        viewModel.users.observe(this, Observer {
            selectGuestsProgressBar.visibility = View.GONE
            listAdapter.submitList(it)
        })
    }
}