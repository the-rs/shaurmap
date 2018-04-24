package com.rightsoftware.shaurmap.utils.extensions

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.animation.AnimationUtils

fun Fragment.loadAnim(resourceId: Int) = AnimationUtils.loadAnimation(context, resourceId)

fun Fragment.getResColor(colorId: Int) = ContextCompat.getColor(context, colorId)