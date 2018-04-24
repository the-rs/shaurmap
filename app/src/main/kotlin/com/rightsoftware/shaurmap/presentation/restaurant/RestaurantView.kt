package com.rightsoftware.shaurmap.presentation.restaurant

import com.arellomobile.mvp.MvpView
import com.google.android.gms.maps.model.LatLng
import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.business.RestaurantDetails
import com.rightsoftware.shaurmap.business.Review

interface RestaurantView : MvpView {
    fun setRestaurantPosition(position: LatLng)
    fun setRestaurantDetails(restaurantDetails: RestaurantDetails)
    fun showError(message: String)
    fun showLoading(loading: Boolean)
    // My review
    fun showReviewPublicationProgress(show: Boolean)
    fun onReviewPublished(review: Review)
    fun setOwnThumbnail(thumbnailUrl: String)
    // Comments
    fun showComments(comments: List<Comment>)
    fun onCommentPublished(comment: Comment)
    fun showCommentPublicationProgress(show: Boolean)

    fun showCommentsRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showCommentsPageProgess(show: Boolean)
    fun showCommentsEmptyView(show: Boolean)
    fun showCommentsEmptyError(show: Boolean, message: String?)
    fun showMessage(message: String)
    fun showComments(show: Boolean, comments: List<Comment>)

    fun onFavoriteStateChanged(addedToFavorites: Boolean)
}