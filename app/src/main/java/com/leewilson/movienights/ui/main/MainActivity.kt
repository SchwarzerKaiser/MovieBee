package com.leewilson.movienights.ui.main

import android.content.Intent
import com.leewilson.movienights.util.BottomNavController
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.auth.AuthActivity
import com.leewilson.movienights.ui.main.feed.FeedItemDetailFragment
import com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment
import com.leewilson.movienights.util.setUpNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener
{

    private val viewModel by viewModels<MainViewModel>()

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

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        bottomNavController.onNavigationItemSelected()
    }

    override fun getNavGraphId(itemId: Int): Int = when(itemId) {
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

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) = when(fragment) {
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

    fun logout() {
        viewModel.logout()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}