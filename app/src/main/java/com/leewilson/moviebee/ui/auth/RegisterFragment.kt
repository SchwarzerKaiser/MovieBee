package com.leewilson.moviebee.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leewilson.moviebee.R
import com.leewilson.moviebee.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        }

        initListeners()
    }

    private fun initListeners() {
        auth_btn_register.setOnClickListener {
            viewModel.setStateEvent(
                AuthStateEvent.RegisterEvent(
                    register_edittext_name.text.toString(),
                    register_edittext_email.text.toString(),
                    register_edittext_password.text.toString(),
                    register_edittext_confirmpassword.text.toString()
                )
            )
        }
    }
}