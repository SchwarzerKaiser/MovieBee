package com.leewilson.movienights.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.auth.AuthActivity
import com.leewilson.movienights.ui.follow.*
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.MainActivity
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseMainFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var picasso: Picasso

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        addListeners()
        subscribeObservers()
        viewModel.setStateEvent(ProfileStateEvent.FetchUserData)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_edit_profile -> {
                findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
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
                event.getContentIfNotHandled()?.let {
                    displayName.text = it.displayName
                    profileBio.text = it.bio
                    followersTextView.text = it.followers.size.toString()
                    followingTextView.text = it.following.size.toString()
                    if(it.imageUri.isNotBlank()) {
                        picasso.load(it.imageUri)
                            .placeholder(R.drawable.default_profile_img)
                            .rotate(270f)
                            .into(profileImageView)
                    }
                }
            }
        })
    }

    private fun addListeners() {
        logoutBtn.setOnClickListener {
            viewModel.removeUserData()
            navToAuthActivity()
        }

        followersTextView.setOnClickListener {
            if ((it as TextView).text == "0") return@setOnClickListener
            val intent = Intent(requireContext(), FollowActivity::class.java)
            intent.putExtra(EXTRA_FOLLOW_USERS_TYPE, FollowUsersType.FOLLOWERS as Parcelable)
            intent.putStringArrayListExtra(EXTRA_USER_IDS, viewModel
                .dataState.value?.data?.peekContent()?.followers)
            requireActivity().startActivity(intent)
        }

        followingTextView.setOnClickListener {
            if ((it as TextView).text == "0") return@setOnClickListener
            val intent = Intent(requireContext(), FollowActivity::class.java)
            intent.putExtra(EXTRA_FOLLOW_USERS_TYPE, FollowUsersType.FOLLOWING as Parcelable)
            intent.putStringArrayListExtra(EXTRA_USER_IDS, viewModel
                .dataState.value?.data?.peekContent()?.following)
            requireActivity().startActivity(intent)
        }
    }

    private fun navToAuthActivity() = (activity as MainActivity).run {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}