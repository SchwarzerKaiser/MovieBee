package com.leewilson.movienights.ui.auth

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.BuildConfig
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.auth.state.AuthStateEvent
import com.leewilson.movienights.util.fadeIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(), AuthActivity.OnMissingUserListener {

    private val TAG = "LoginFragment"

    private lateinit var viewModel: AuthViewModel

    val animationDuration = 1000L
    val offset = 300L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth_register_text_link.setText(Html.fromHtml(getString(R.string.register_account_string)))

        activity?.run {
            viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
            (this as AuthActivity).onMissingUserListener = this@LoginFragment
        }

        if (!viewModel.userExists) {
            showInputFields()
        }

        doLogoAnimation()
        initListeners()
        attemptLoginWithExistingUser()
    }

    private fun doLogoAnimation() {
        val logoAnimation = ObjectAnimator.ofFloat(
            login_screen_logo, View.TRANSLATION_Y, -100f
        ).apply { duration = animationDuration }

        val appTitleAnimation = ObjectAnimator.ofFloat(
            appTitle, View.TRANSLATION_Y, -100f
        ).apply { duration = animationDuration }

        logoAnimation.start()
        appTitleAnimation.start()
    }

    private fun attemptLoginWithExistingUser() {
        viewModel.setStateEvent(AuthStateEvent.ExistingUserLoginEvent)
    }

    private fun initListeners() {
        auth_register_text_link.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        auth_btn_login.setOnClickListener {
            viewModel.setStateEvent(
                AuthStateEvent.LoginEvent(
                    auth_email_field.text.toString(),
                    auth_password_field.text.toString()
                )
            )
        }
    }

    override fun onUserMissing() {
        viewModel.userExists = false
        if ((activity as AuthActivity).firstTimeAnimationPlayed) return

        showInputFields()

        auth_email_input.fadeIn(animationDuration, offset)
        auth_password_input.fadeIn(animationDuration, offset)
        auth_btn_login.fadeIn(animationDuration, offset)
        auth_register_text_link.fadeIn(animationDuration, offset)
    }

    private fun showInputFields() {
        auth_email_input.visibility = View.VISIBLE
        auth_password_input.visibility = View.VISIBLE
        auth_btn_login.visibility = View.VISIBLE
        auth_register_text_link.visibility = View.VISIBLE
    }
}