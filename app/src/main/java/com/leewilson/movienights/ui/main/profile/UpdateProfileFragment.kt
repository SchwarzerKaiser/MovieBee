package com.leewilson.movienights.ui.main.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_update_profile.*

@AndroidEntryPoint
class UpdateProfileFragment : BaseMainFragment(R.layout.fragment_update_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        subscribeObservers()
        viewModel.setStateEvent(ProfileStateEvent.FetchUserData)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_update_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_save_profile -> {
                viewModel.setStateEvent(
                    ProfileStateEvent.UpdateUserData(
                        displayName = updateProfileNameField.text.toString(),
                        email = updateProfileEmailField.text.toString(),
                        bio = updateProfileBioField.text.toString()
                    )
                )
                return true
            }
        }
        return false
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { state ->
            showProgressBar(state.loading)
            hideFragmentRootView(state.loading)
            state.message?.let { event ->
                event.getContentIfNotHandled()?.let { message ->
                    showSnackbar(message)
                }
            }
            state.data?.let { event ->
                event.getContentIfNotHandled()?.let { viewState ->
                    updateProfileNameField.setText(viewState.displayName)
                    updateProfileEmailField.setText(viewState.email)
                    updateProfileBioField.setText(viewState.bio)
                }
            }
        })
    }
}