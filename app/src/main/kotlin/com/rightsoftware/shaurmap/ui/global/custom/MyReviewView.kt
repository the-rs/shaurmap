package com.rightsoftware.shaurmap.ui.global.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.like.LikeButton
import com.like.OnLikeListener
import com.rightsoftware.shaurmap.GlideApp
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.utils.extensions.*
import kotlinx.android.synthetic.main.view_my_review.view.*

class MyReviewView(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    init {
        inflate(context, R.layout.view_my_review, this)

        btnTasteLike.setOnLikeListener(object: OnLikeListener{
            override fun liked(p0: LikeButton) { btnTasteDislike.isLiked = false }
            override fun unLiked(p0: LikeButton) {}
        })
        btnTasteDislike.setOnLikeListener(object: OnLikeListener{
            override fun liked(p0: LikeButton) { btnTasteLike.isLiked = false }
            override fun unLiked(p0: LikeButton) {}
        })
        btnServiceLike.setOnLikeListener(object: OnLikeListener{
            override fun liked(p0: LikeButton) { btnServiceDislike.isLiked = false }
            override fun unLiked(p0: LikeButton) {}
        })
        btnServiceDislike.setOnLikeListener(object: OnLikeListener{
            override fun liked(p0: LikeButton) { btnServiceLike.isLiked = false }
            override fun unLiked(p0: LikeButton) {}
        })
    }

    enum class DisplayMode {
        PREVIEW,
        SUBMIT
    }

    var rating: Int
        get() = rbMyReview.rating.toInt()
        set(value) {
            if(value !in 1..5) throw IllegalArgumentException("Rating should be in range from 1 to 5")
            rbMyReview.rating = value.toFloat()
        }

    var taste: Int?
        get(){
            return when {
                btnTasteLike.isLiked -> 1
                btnTasteDislike.isLiked -> 0
                else -> null
            }
        }
        set(value){
            when(value){
                1 -> btnTasteLike.isLiked = true
                0 -> btnTasteDislike.isLiked = true
                null -> return
            }
        }

    var service: Int?
        get(){
            return when {
                btnServiceLike.isLiked -> 1
                btnServiceDislike.isLiked -> 0
                else -> null
            }
        }
        set(value){
            when(value){
                1 -> btnServiceLike.isLiked = true
                0 -> btnServiceDislike.isLiked = true
                null -> return
            }
        }

    private fun enterPreviewMode(animate: Boolean) {
        if(animate){
            rbMyReview.isIndicator = true
            btnTasteLike.isEnabled = false
            btnTasteDislike.isEnabled = false
            btnServiceLike.isEnabled = false
            btnServiceDislike.isEnabled = false

            tvRateThisRestaurant.animateFadeOut(500, goneAfter = false)
            tvReviewDate.animateFadeIn(500)
            ivBtnEdit.animateFadeIn(500)
            ivBtnDelete.animateFadeIn(500)
            btnSubmitReview.animateCollapse(500, 200)
        }
        else {
            rbMyReview.isIndicator = true
            btnTasteLike.isEnabled = false
            btnTasteDislike.isEnabled = false
            btnServiceLike.isEnabled = false
            btnServiceDislike.isEnabled = false

            tvRateThisRestaurant.visibility = View.INVISIBLE // constraint
            btnSubmitReview.visible(false)

            tvReviewDate.visible(true)
            ivBtnEdit.visible(true)
            ivBtnDelete.visible(true)
        }
    }

    private fun enterSubmitMode(animate: Boolean) {
        if(animate) {
            rbMyReview.isIndicator = false
            btnTasteLike.isEnabled = true
            btnTasteDislike.isEnabled = true
            btnServiceLike.isEnabled = true
            btnServiceDislike.isEnabled = true

            tvReviewDate.animateFadeOut(500)
            ivBtnDelete.animateFadeOut(500)
            ivBtnEdit.animateFadeOut(500)

            tvRateThisRestaurant.animateFadeIn(500)
            btnSubmitReview.animateExpand(500)
        }
        else {
            rbMyReview.isIndicator = false
            btnTasteLike.isEnabled = true
            btnTasteDislike.isEnabled = true
            btnServiceLike.isEnabled = true
            btnServiceDislike.isEnabled = true

            tvReviewDate.visible(false)
            ivBtnDelete.visible(false)
            ivBtnEdit.visible(false)

            tvRateThisRestaurant.visible(true)
            btnSubmitReview.visible(true)
        }
    }

    fun setDisplayMode(displayMode: DisplayMode, animate: Boolean = false) {
        if(displayMode == DisplayMode.PREVIEW) enterPreviewMode(animate) else enterSubmitMode(animate)
    }

    fun setOnSubmitClickListener(clickListener: (MyReviewView) -> Unit) {
        btnSubmitReview.setOnClickListener { clickListener(this) }
    }

    fun setOnEditClickListener(clickListener: (MyReviewView) -> Unit) {
        ivBtnEdit.setOnClickListener { clickListener(this) }
    }

    fun setOnDeleteClickListener(clickListener: (MyReviewView) -> Unit) {
        ivBtnDelete.setOnClickListener { clickListener(this) }
    }

    fun resetValues(){
        btnTasteLike.isLiked  = false
        btnTasteDislike.isLiked = false
        btnServiceLike.isLiked = false
        btnServiceDislike.isLiked = false
        rbMyReview.rating = 3f
    }

    fun setThumbnailUrl(url: String){
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions().circleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.avatar_placeholder_circle)
                .into(ivOwnThumbnail)
    }

    fun setReviewDate(dateString: String) {
        tvReviewDate.text = dateString
    }
}