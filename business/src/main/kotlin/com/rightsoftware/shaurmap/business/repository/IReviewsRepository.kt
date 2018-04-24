package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.business.SubmitReview
import io.reactivex.Single

interface IReviewsRepository {
    fun getReviews(restaurantId: String, page: Int, latestReviewTimestamp: Long?) : Single<List<Review>>
    fun submitReview(review: SubmitReview) : Single<Review>
}