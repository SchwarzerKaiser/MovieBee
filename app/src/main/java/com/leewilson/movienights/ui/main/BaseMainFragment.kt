package com.leewilson.movienights.ui.main

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

open class BaseMainFragment(@LayoutRes resId: Int): Fragment(resId) {

    fun showProgressBar(showing: Boolean) {
        (activity as MainActivity).run {
            mainProgressBar.visibility  =
                if (showing) View.VISIBLE else View.INVISIBLE
        }
    }

    fun showSnackbar(message: String) {
        (activity as MainActivity).run {
            Snackbar.make(mainFragmentContainer, message, Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}