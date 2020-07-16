package com.leewilson.movienights.ui.main.newpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.BaseMainFragment

class SearchMovieFragment : BaseMainFragment(R.layout.fragment_searchmovie) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
    }

    private fun setupNavigationUI() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.searchMovieFragment
            )
        )
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            appBarConfiguration
        )
    }
}