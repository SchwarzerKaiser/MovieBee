package com.leewilson.movienights.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leewilson.movienights.R
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempLinkToDetailFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_feedItemDetailFragment)
        }
    }
}