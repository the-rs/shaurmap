package com.rightsoftware.shaurmap.presentation.reviews

import com.arellomobile.mvp.MvpView
import com.rightsoftware.shaurmap.business.Review

interface ReviewsListView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showMessage(message: String)

    fun showReviews(show: Boolean, reviews: List<Review>)
}