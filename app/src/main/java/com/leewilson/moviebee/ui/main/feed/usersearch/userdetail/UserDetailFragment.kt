package com.leewilson.moviebee.ui.main.feed.usersearch.userdetail

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.leewilson.moviebee.R
import com.leewilson.moviebee.databinding.FragmentUserDetailBinding
import com.leewilson.moviebee.ui.main.BaseMainFragment
import com.leewilson.moviebee.ui.main.feed.usersearch.state.UserDetailStateEvent
import com.leewilson.moviebee.util.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

const val USER_ARG = "com.leewilson.movienights.ui.main.feed.usersearch.USER_ARG"
private const val TAG = "UserDetail"

@AndroidEntryPoint
class UserDetailFragment : BaseMainFragment(R.layout.fragment_user_detail) {

    @Inject lateinit var sharedPreferences: SharedPreferences
    private var myUid: String? = null

    private val viewModel: UserDetailViewModel by viewModels()

    private var _binding: FragmentUserDetailBinding? = null
    private val binding: FragmentUserDetailBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myUid = sharedPreferences.getString(Constants.CURRENT_USER_UID, null)
        arguments?.let { args ->
            val uid = args.getString(USER_ARG)
            uid?.let { viewModel.setStateEvent(UserDetailStateEvent.GetUserDetail(it)) }
        }
        subscribeObservers()
        setListeners()
    }

    private fun setListeners() {
        binding.followBtn.setOnClickListener {
            viewModel.dataState.value?.data?.peekContent()?.let { user ->
                user.uid?.let { id ->
                    viewModel.setStateEvent(
                        UserDetailStateEvent.FollowButton(id)
                    )
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            showProgressBar(dataState.loading)
            dataState.message?.let { consumableMsg ->
                consumableMsg.getContentIfNotHandled()?.let { msg ->
                    showSnackbar(msg)
                }
            }
            dataState.data?.let { consumableData ->
                consumableData.peekContent().let { user ->

                    Log.d(TAG, "Loaded user: $user")

                    binding.run {
                        displayName.text = user.displayName
                        followersTextView.text = user.followers?.size.toString()
                        followingTextView.text = user.following?.size.toString()
                        profileBio.text = user.bio
                    }

                    Picasso.get()
                        .load(user.imageUri)
                        .placeholder(R.drawable.default_profile_img)
                        .into(binding.profileImageView)

                    user.followers?.let { followers ->
                        if (followers.contains(myUid)) {
                            // already following. Show unfollow btn
                            binding.run {
                                followBtn.text = "Unfollow"
                                followBtn.icon = requireContext()
                                    .getDrawable(R.drawable.ic_baseline_delete_24)
                                followBtn.setBackgroundColor(requireContext().getColor(android.R.color.holo_red_light))
                            }
                        } else {
                            // Show follow btn
                            binding.run {
                                followBtn.text = "Follow"
                                followBtn.icon = requireContext()
                                    .getDrawable(R.drawable.ic_baseline_person_add_24)
                                followBtn.setBackgroundColor(requireContext().getColor(R.color.colorPrimary))
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}