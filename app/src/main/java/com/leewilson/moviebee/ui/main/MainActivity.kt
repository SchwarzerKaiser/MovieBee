package com.leewilson.moviebee.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leewilson.moviebee.R
import com.leewilson.moviebee.ui.main.feed.FeedItemDetailFragment
import com.leewilson.moviebee.ui.main.newpost.CreateMovieNightFragment
import com.leewilson.moviebee.ui.main.profile.UpdateProfileFragment
import com.leewilson.moviebee.util.BottomNavController
import com.leewilson.moviebee.util.setUpNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

private const val SAVE_STATE_BACKSTACK = "SAVE_STATE_BACKSTACK"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationReselectedListener
{

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            context = this,
            containerId = R.id.mainFragmentContainer,
            appStartDestinationId = R.id.bottom_nav_menu_feed,
            navGraphProvider = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        savedInstanceState?.let { savedState ->
            savedState.getIntegerArrayList(SAVE_STATE_BACKSTACK)?.let {
                bottomNavController.restoreState(it)
            }
        }
        bottomNavController.onNavigationItemSelected()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntegerArrayList(SAVE_STATE_BACKSTACK, bottomNavController.getState())
        super.onSaveInstanceState(outState)
    }

    override fun getNavGraphId(itemId: Int): Int = when (itemId) {
        R.id.bottom_nav_menu_feed -> R.navigation.feed_nav_graph
        R.id.bottom_nav_menu_new -> R.navigation.newitem_nav_graph
        R.id.bottom_nav_menu_calendar -> R.navigation.calendar_nav_graph
        R.id.bottom_nav_menu_profile -> R.navigation.profile_nav_graph
        else -> R.navigation.feed_nav_graph
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                bottomNavController.onBackPressed()
            }
        }
        return false
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) =
        when (fragment) {
            is FeedItemDetailFragment -> {
                navController.navigate(R.id.action_feedItemDetailFragment_to_feedFragment)
            }

            is CreateMovieNightFragment -> {
                navController.navigate(R.id.action_createMovieNightFragment_to_searchMovieFragment)
            }

            is UpdateProfileFragment -> {
                navController.navigate(R.id.action_updateProfileFragment_to_profileFragment)
            }

            else -> {
                // Do nothing.
            }
        }
}