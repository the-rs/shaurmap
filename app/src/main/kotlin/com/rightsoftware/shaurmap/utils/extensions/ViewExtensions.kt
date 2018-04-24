package com.rightsoftware.shaurmap.utils.extensions

import android.animation.*
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.support.annotation.LayoutRes
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.ui.global.custom.MyReviewView


fun ImageView.setImageBytes(imageBytes : ByteArray){
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    this.setImageBitmap(bitmap)
}

fun ImageView.setTint(colorId: Int){
    setColorFilter(colorId, PorterDuff.Mode.SRC_IN)
}

@SuppressLint("ObjectAnimatorBinding")
fun FloatingActionButton.animateChangeColor(colorFrom: Int, colorTo: Int, duration: Long){
    ObjectAnimator.ofInt(this, "backgroundTint", colorFrom, colorTo).apply {
        this@apply.duration = duration
        setEvaluator(ArgbEvaluator())
        interpolator = DecelerateInterpolator(2f)
        addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            this@animateChangeColor.backgroundTintList = ColorStateList.valueOf(animatedValue)
        }
    }.start()
}

fun View.animateFadeIn(animationDuration: Long){
    ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
        duration = animationDuration
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                visible(true)
            }
        })
    }.start()
}

fun View.animateFadeOut(animationDuration: Long, goneAfter: Boolean = true){
    ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
        duration = animationDuration
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                visibility = if(goneAfter) View.GONE else View.INVISIBLE
            }
        })
    }.start()
}

fun View.animateExpand(animationDuration: Long){
    measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val targetHeight = measuredHeight

    ValueAnimator.ofInt(1, targetHeight).apply {
        addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                visible(true)
                layoutParams.height = 0
            }
        })
        addUpdateListener { animation ->
            layoutParams.height = animation.animatedValue as Int
            requestLayout()
        }
        interpolator = DecelerateInterpolator()
        duration = animationDuration
    }.start()
}

fun View.animateCollapse(animationDuration: Long, delay: Long = 0){
    ValueAnimator.ofInt(height, 0).apply {
        interpolator = DecelerateInterpolator()
        addUpdateListener { animation ->
            layoutParams.height = animation.animatedValue as Int
            requestLayout()
        }
        addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                visible(false)
            }
        })
        interpolator = DecelerateInterpolator()
        startDelay = delay
        duration = animationDuration
    }.start()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visible(visible: Boolean) {
    visibility = if(visible) View.VISIBLE else View.GONE
}

fun MyReviewView.setReview(review: Review){
    with(review){
        setReviewDate(date.toLocalizedShortDateString())
        this@setReview.rating = rating
        this@setReview.taste = taste
        this@setReview.service = service
    }
}

fun View.removeFocus() {
    isFocusableInTouchMode = false
    isFocusable = false

    isFocusableInTouchMode = true
    isFocusable = true
}


