package com.rightsoftware.shaurmap.data.restaurants

import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.repository.IRestaurantsRepository
import com.rightsoftware.shaurmap.data.utils.mappers.DomainDbMapper
import com.rightsoftware.shaurmap.data.restaurants.cache.RestaurantsCache
import com.rightsoftware.shaurmap.data.ShaurmapApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RestaurantsRepository @Inject constructor(
        private val restaurantsCache: RestaurantsCache,
        private val shaurmapApi: ShaurmapApi,
        private val domainDbMapper: DomainDbMapper
) : IRestaurantsRepository {
    override fun pullRestaurants(): Single<List<Restaurant>> {
        return shaurmapApi.getRestaurants()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::addRestaurantsToCache)
    }

    override fun getRestaurantsFromCache(): Single<List<Restaurant>> {
        return restaurantsCache.getRestaurants()
                .flatMap {
                    Observable.fromIterable(it)
                            .map { domainDbMapper.convertToDomainModel(it) }
                            .toList()
                }
    }

    override fun createRestaurant(restaurant: Restaurant): Single<Restaurant> {
        return shaurmapApi.postRestaurant(restaurant)
                .doOnSuccess(this::addRestaurantToCache)
    }

     private fun addRestaurantsToCache(restaurants: List<Restaurant>)  {
        Observable.fromIterable(restaurants)
                .observeOn(Schedulers.computation())
                .map { domainDbMapper.convertToDbModel(it) }
                .toList()
                .observeOn(Schedulers.io())
                .subscribe(restaurantsCache::updateRestaurants)
    }

    private fun addRestaurantToCache(restaurant: Restaurant)  {
        restaurantsCache.addRestaurant(domainDbMapper.convertToDbModel(restaurant))
    }
}