package com.leewilson.moviebee.ui.main.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.leewilson.moviebee.R
import com.leewilson.moviebee.ui.main.BaseMainFragment

class CalendarFragment : BaseMainFragment(R.layout.fragment_calendar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
    }

    private fun setupNavigationUI() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment
            )
        )
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            appBarConfiguration
        )
    }
}