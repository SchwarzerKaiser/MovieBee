package com.leewilson.movienights.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.auth.AuthActivity
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.MainActivity
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment : BaseMainFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners()
        subscribeObservers()
        viewModel.setStateEvent(ProfileStateEvent.FetchUserData)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { state ->
            showProgressBar(state.loading)
            state.message?.let { event ->
                event.getContentIfNotHandled()?.let { message ->
                    showSnackbar(message)
                }
            }
            state.data?.let { event ->
                event.getContentIfNotHandled()?.let {
                    displayName.text = it.displayName
                    profileBio.text = it.bio
                }
            }
        })
    }

    private fun addListeners() {
        logoutBtn.setOnClickListener {
            viewModel.removeUserData()
            navToAuthActivity()
        }
    }

    private fun navToAuthActivity() = (activity as MainActivity).run {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}