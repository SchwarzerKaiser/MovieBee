package com.leewilson.movienights.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.leewilson.movienights.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Base Fragment class for the main navigation flows.
 *
 * @param resId The layout resource id for the Fragment
 */
abstract class BaseMainFragment(@LayoutRes resId: Int) : Fragment(resId) {

    private var rootHasBeenHidden = false;

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
                if (showing) View.VISIBLE else View.GONE
        }
    }

    fun hideSoftKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideFragmentRootView(hideView: Boolean) {
        if (hideView) {
            if (!rootHasBeenHidden) {
                view?.visibility = View.INVISIBLE
                rootHasBeenHidden = true
            }
        } else {
            view?.visibility = View.VISIBLE
        }
    }

    fun showSnackbar(message: String) {
        (activity as MainActivity).run {
            Snackbar.make(mainFragmentContainer, message, Snackbar.LENGTH_SHORT)
                .setAnchorView(findViewById<BottomNavigationView>(R.id.bottomNavigation))
                .show()
        }
    }
}