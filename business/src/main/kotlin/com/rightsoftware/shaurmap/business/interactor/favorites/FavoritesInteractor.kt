package com.rightsoftware.shaurmap.business.interactor.favorites

import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.repository.IFavoritesRepository
import io.reactivex.Single
import javax.inject.Inject

class FavoritesInteractor @Inject constructor(
        private val favoritesRepository: IFavoritesRepository,
        private val schedulers: SchedulersProvider
){
    fun getFavoritesRestaurants(page: Int, latestTimestamp: Long? = null) : Single<List<FavoriteRestaurant>> {
        return favoritesRepository.getFavoritesRestaurants(page, latestTimestamp)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }
}