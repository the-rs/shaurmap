package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import io.reactivex.Completable
import io.reactivex.Single

interface IFavoritesRepository {
    fun getFavoritesRestaurants(page: Int, latestTimestamp: Long?) : Single<List<FavoriteRestaurant>>
    fun addRestaurantToFavorites(restaurantId: String) : Single<FavoriteRestaurant>
    fun removeRestaurantFromFavorites(restaurantId: String) : Completable
}