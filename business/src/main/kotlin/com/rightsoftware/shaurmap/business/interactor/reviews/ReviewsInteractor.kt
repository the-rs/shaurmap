package com.rightsoftware.shaurmap.business.interactor.reviews

import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.repository.IReviewsRepository
import io.reactivex.Single
import javax.inject.Inject

class ReviewsInteractor @Inject constructor(
        private val reviewsRepository: IReviewsRepository,
        private val schedulers: SchedulersProvider
) {
    fun getReviews(
            restaurantId: String,
            page: Int,
            latestReviewTimestamp: Long? = null) : Single<List<Review>> {
        return reviewsRepository.getReviews(restaurantId,page,latestReviewTimestamp)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }
}