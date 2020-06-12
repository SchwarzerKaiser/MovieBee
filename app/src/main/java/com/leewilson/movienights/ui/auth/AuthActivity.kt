package com.leewilson.movienights.ui.auth

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.leewilson.movienights.BaseApplication
import com.leewilson.movienights.R
import com.leewilson.movienights.di.DaggerApplicationComponent
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    private val TAG = "AuthActivity"

    @Inject
    lateinit var authViewModelProviderFactory: AuthViewModelProviderFactory

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        (applicationContext as BaseApplication)
            .appComponent
            .authComponent()
            .create()
            .inject(this)

        viewModel = ViewModelProvider(this, authViewModelProviderFactory)
            .get(AuthViewModel::class.java)

        Log.d(TAG, viewModel.toString())
    }
}