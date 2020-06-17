package com.leewilson.movienights.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.R
import com.leewilson.movienights.di.auth.AuthComponent
import com.leewilson.movienights.ui.auth.state.AuthStateEvent
import com.leewilson.movienights.ui.main.MainActivity
import com.leewilson.movienights.util.Constants.Companion.UID_STRING_EXTRA
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    private val TAG = "AuthActivity"

    @Inject
    lateinit var authViewModelProviderFactory: AuthViewModelProviderFactory
    lateinit var authComponent: AuthComponent
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authComponent = (applicationContext as BaseApplication)
            .appComponent
            .authComponent()
            .create()
        authComponent.inject(this)

        viewModel = ViewModelProvider(this, authViewModelProviderFactory)
            .get(AuthViewModel::class.java)

        subscribeObservers()
        attemptLogin()
    }

    private fun attemptLogin() {
        viewModel.setStateEvent(
            AuthStateEvent.ExistingUserLoginEvent
        )
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            dataState?.let { ds ->
                showProgressBar(ds.loading)
                ds.data?.let { event ->
                    val content = event.getContentIfNotHandled()
                    content?.let { viewState ->
                        val uid = viewState.uid
                        navToMainActivity(uid)
                    }
                }

                ds.message?.let { event ->
                    val message = event.getContentIfNotHandled()
                    message?.let { showSnackbar(it) }
                }
            }
        })
    }

    private fun navToMainActivity(uid: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(UID_STRING_EXTRA, uid)
        startActivity(intent)
    }

    private fun showProgressBar(isVisible: Boolean) {
        if(isVisible) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            auth_activity_root_view,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}