package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.Restaurant
import io.reactivex.Completable
import io.reactivex.Single

interface IRestaurantsRepository {

    fun pullRestaurants() : Single<List<Restaurant>>
    fun getRestaurantsFromCache() : Single<List<Restaurant>>
    fun createRestaurant(restaurant: Restaurant) : Single<Restaurant>
}