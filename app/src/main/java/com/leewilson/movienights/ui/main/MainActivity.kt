package com.leewilson.movienights.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.feed.FeedItemDetailFragment
import com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment
import com.leewilson.movienights.util.BottomNavController
import com.leewilson.movienights.util.setUpNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            context = this,
            containerId = R.id.mainFragmentContainer,
            appStartDestinationId = R.id.bottom_nav_menu_feed,
            graphChangeListener = this,
            navGraphProvider = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        bottomNavController.onNavigationItemSelected()
    }

    override fun getNavGraphId(itemId: Int): Int = when (itemId) {
        R.id.bottom_nav_menu_feed -> R.navigation.feed_nav_graph
        R.id.bottom_nav_menu_new -> R.navigation.newitem_nav_graph
        R.id.bottom_nav_menu_calendar -> R.navigation.calendar_nav_graph
        R.id.bottom_nav_menu_profile -> R.navigation.profile_nav_graph
        else -> R.navigation.feed_nav_graph
    }

    override fun onGraphChange() {
        // Do nothing for now
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

            else -> {
                // Do nothing.
            }
        }
}