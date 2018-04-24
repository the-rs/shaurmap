package com.rightsoftware.shaurmap.data.reviews

import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.business.SubmitReview
import com.rightsoftware.shaurmap.business.repository.IReviewsRepository
import com.rightsoftware.shaurmap.data.ShaurmapApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReviewsRepository @Inject constructor(
        private val shaurmapApi: ShaurmapApi
) : IReviewsRepository {
    override fun getReviews(
            restaurantId: String,
            page: Int,
            latestReviewTimestamp: Long?): Single<List<Review>> {
        return shaurmapApi.getReviews(restaurantId, page, latestReviewTimestamp)
    }

    override fun submitReview(review: SubmitReview): Single<Review> {
        return shaurmapApi.postReview(review)
    }
}