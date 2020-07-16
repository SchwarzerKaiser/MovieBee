package com.leewilson.movienights.ui.main.feed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : BaseMainFragment(R.layout.fragment_feed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempLinkToDetailFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_feedItemDetailFragment)
        }

        setupNavigationUI()
    }

    private fun setupNavigationUI() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.feedFragment
            )
        )
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            appBarConfiguration
        )
    }
}