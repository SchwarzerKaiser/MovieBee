package com.leewilson.movienights.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    @Inject
    lateinit var providerFactory: AuthViewModelProviderFactory

    private lateinit var viewModel: AuthViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AuthActivity).authComponent
            .inject(this)
    }

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

        initViewModel()
        initListeners()
        subscribeObservers()
        attemptLoginWithExistingUser()
    }

    private fun attemptLoginWithExistingUser() {
        viewModel.setStateEvent(AuthStateEvent.ExistingUserLoginEvent)
    }

    private fun subscribeObservers() {
        // subscribe to LiveData
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity().viewModelStore, providerFactory)
            .get(AuthViewModel::class.java)
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
}