package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.RestaurantDetails
import io.reactivex.Single


interface IRestaurantDetailsRepository {
    fun getCachedRestaurant(id: String) : Single<RestaurantDetails>
    fun getRestaurantDetails(id: String) : Single<RestaurantDetails>
}