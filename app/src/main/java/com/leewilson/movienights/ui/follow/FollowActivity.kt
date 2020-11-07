package com.leewilson.movienights.ui.follow

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leewilson.movienights.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_follow.*

@Parcelize
enum class FollowUsersType : Parcelable {
    FOLLOWERS, FOLLOWING
}

const val EXTRA_FOLLOW_USERS_TYPE = "EXTRA_FOLLOW_USERS_TYPE"
const val EXTRA_USER_IDS = "EXTRA_USER_IDS"

private const val TAG = "FollowActivity"

@AndroidEntryPoint
class FollowActivity : AppCompatActivity() {

    private val viewModel: FollowViewModel by viewModels()

    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)

        setupRecyclerView()
        subscribeObservers()

        intent?.let { intent ->
            val type = intent.getParcelableExtra<FollowUsersType>(EXTRA_FOLLOW_USERS_TYPE)
            when (type) {
                FollowUsersType.FOLLOWERS -> toolbar.title = "Followers"
                FollowUsersType.FOLLOWING -> toolbar.title = "Following"
            }
            val data = intent.getStringArrayListExtra(EXTRA_USER_IDS)
            if (!data.isNullOrEmpty()) {
                viewModel.fetchUsers(data)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapter()
        usersRecyclerView.adapter = adapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun subscribeObservers() {
        viewModel.users.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}