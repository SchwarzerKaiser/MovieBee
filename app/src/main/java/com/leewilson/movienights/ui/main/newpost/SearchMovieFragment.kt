package com.leewilson.movienights.ui.main.newpost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.leewilson.movienights.R
import com.leewilson.movienights.api.OMDBService
import com.leewilson.movienights.ui.main.BaseMainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieFragment : BaseMainFragment(R.layout.fragment_searchmovie) {

    private val TAG = "AppDebug"

    @Inject
    lateinit var apiService: OMDBService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}