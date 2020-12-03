package com.leewilson.movienights.util

import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.leewilson.movienights.R

fun View.fadeIn(animTime: Long, startOffset: Long) {
    val fadeIn = AnimationUtils.loadAnimation(context, R.anim.slower_fade_in).apply {
        duration = animTime
        interpolator = LinearInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(fadeIn)
}