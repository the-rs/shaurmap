package com.rightsoftware.shaurmap.di.modules

import com.rightsoftware.shaurmap.business.interactor.restaurant.RestaurantDetailsInteractor
import com.rightsoftware.shaurmap.business.interactor.reviews.ReviewsInteractor
import com.rightsoftware.shaurmap.business.repository.ICommentsRepository
import com.rightsoftware.shaurmap.business.repository.IRestaurantDetailsRepository
import com.rightsoftware.shaurmap.business.repository.IReviewsRepository
import com.rightsoftware.shaurmap.data.comments.CommentsRepository
import com.rightsoftware.shaurmap.data.restaurants.RestaurantDetailsRepository
import com.rightsoftware.shaurmap.data.reviews.ReviewsRepository
import toothpick.config.Module

class RestaurantDetailsModule : Module() {
    init {
        bind(IRestaurantDetailsRepository::class.java).to(RestaurantDetailsRepository::class.java)
        bind(RestaurantDetailsInteractor::class.java)

        bind(IReviewsRepository::class.java).to(ReviewsRepository::class.java)
        bind(ReviewsInteractor::class.java)

        bind(ICommentsRepository::class.java).to(CommentsRepository::class.java)
    }
}