package com.rightsoftware.shaurmap.business.interactor.restaurant

import com.rightsoftware.shaurmap.business.*
import com.rightsoftware.shaurmap.business.extensions.round
import com.rightsoftware.shaurmap.business.repository.*
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RestaurantDetailsInteractor @Inject constructor(
        private val restaurantDetailsRepository: IRestaurantDetailsRepository,
        private val reviewsRepository: IReviewsRepository,
        private val profileRepository: IProfileRepository,
        private val commentsRepository: ICommentsRepository,
        private val favoritesRepository: IFavoritesRepository,
        private val schedulers: SchedulersProvider
) {
    fun getCachedRestaurant(id: String) = restaurantDetailsRepository.getCachedRestaurant(id)
    fun getRestaurantDetails(id: String) : Single<RestaurantDetails> {
        return restaurantDetailsRepository.getRestaurantDetails(id)
                .map { it.copy(avgRating = it.avgRating.round(1)) }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    // todo mb return refreshed restaurant details on backend
    fun submitReview(review: SubmitReview) : Single<Review> {
        return if(!review.isValid()) {
            // todo user message
            Single.error(Exception("Something wrong"))
        }
        else {
            reviewsRepository.submitReview(review)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
        }
    }

    fun getOwnThumbnailUrl() : Single<String> {
        return profileRepository.getUserInfo()
                .map { it.imageSource.thumbnailUrl }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun getComments(
            restaurantId: String,
            page: Int,
            latestCommentTimestamp: Long? = null) : Single<List<Comment>> {
        return commentsRepository.getComments(restaurantId, page, latestCommentTimestamp)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun postComment(comment: SubmitComment) : Single<Comment> {
        return if(!comment.isValid()) {
            // todo user message
            Single.error(Exception("Something wrong"))
        }
        else {
            commentsRepository.postComment(comment)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
        }
    }

    fun addRestaurantToFavorites(restaurantId: String) : Single<FavoriteRestaurant> {
        return favoritesRepository.addRestaurantToFavorites(restaurantId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun removeRestaurantFromFavorites(restaurantId: String) : Completable {
        return favoritesRepository.removeRestaurantFromFavorites(restaurantId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    private fun SubmitReview.isValid() =
            rating in (1..5) && (taste == null || taste in (0..1)) && (service == null || service in (0..1))

    private fun SubmitComment.isValid() = text.length <= 300
}