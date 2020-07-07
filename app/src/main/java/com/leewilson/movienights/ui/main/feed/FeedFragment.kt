package com.leewilson.movienights.ui.main.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leewilson.movienights.R
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment(R.layout.fragment_feed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempLinkToDetailFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_feedItemDetailFragment)
        }
    }
}