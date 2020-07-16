package com.leewilson.movienights.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.leewilson.movienights.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Base Fragment class for the main navigation flows.
 *
 * @param resId The layout resource id for the Fragment
 */
open class BaseMainFragment(@LayoutRes resId: Int) : Fragment(resId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUI()
    }

    private fun setupNavUI() {
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            AppBarConfiguration(setOf(
                R.id.profileFragment,
                R.id.calendarFragment,
                R.id.searchMovieFragment,
                R.id.feedFragment
            ))
        )
    }

    fun showProgressBar(showing: Boolean) {
        (activity as MainActivity).run {
            mainProgressBar.visibility =
                if (showing) View.VISIBLE else View.INVISIBLE
        }
    }

    fun showSnackbar(message: String) {
        (activity as MainActivity).run {
            Snackbar.make(mainFragmentContainer, message, Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}