package com.rightsoftware.shaurmap.data.favorites

import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import com.rightsoftware.shaurmap.business.repository.IFavoritesRepository
import com.rightsoftware.shaurmap.data.ShaurmapApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
        private val shaurmapApi: ShaurmapApi
) : IFavoritesRepository{
    override fun getFavoritesRestaurants(page: Int, latestTimestamp: Long?): Single<List<FavoriteRestaurant>> {
        return shaurmapApi.getFavoriteRestaurants(page, latestTimestamp)
    }

    override fun addRestaurantToFavorites(restaurantId: String): Single<FavoriteRestaurant> {
        return shaurmapApi.addRestaurantToFavorites(restaurantId)
    }

    override fun removeRestaurantFromFavorites(restaurantId: String): Completable {
        return shaurmapApi.removeRestaurantFromFavorites(restaurantId)
    }
}