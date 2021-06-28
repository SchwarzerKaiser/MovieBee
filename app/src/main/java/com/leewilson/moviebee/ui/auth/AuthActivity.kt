package com.leewilson.moviebee.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.leewilson.moviebee.R
import com.leewilson.moviebee.ui.auth.state.AuthStateEvent
import com.leewilson.moviebee.ui.main.MainActivity
import com.leewilson.moviebee.util.Constants.Companion.UID_STRING_EXTRA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    var onMissingUserListener: OnMissingUserListener? = null

    var firstTimeAnimationPlayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

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
                        if (viewState.userExists) {
                            val uid = viewState.uid
                            navToMainActivity(uid)
                        } else {
                            onMissingUserListener?.onUserMissing()
                        }
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
        finish()
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

    interface OnMissingUserListener {
        fun onUserMissing()
    }
}