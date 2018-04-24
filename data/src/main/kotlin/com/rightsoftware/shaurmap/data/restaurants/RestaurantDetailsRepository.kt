package com.rightsoftware.shaurmap.data.restaurants

import com.rightsoftware.shaurmap.business.RestaurantDetails
import com.rightsoftware.shaurmap.business.repository.IRestaurantDetailsRepository
import com.rightsoftware.shaurmap.data.restaurants.cache.RestaurantsCache
import com.rightsoftware.shaurmap.data.ShaurmapApi
import com.rightsoftware.shaurmap.data.utils.mappers.DomainDbMapper
import io.reactivex.Single
import javax.inject.Inject

class RestaurantDetailsRepository @Inject constructor(
        private val shaurmapApi: ShaurmapApi,
        private val restaurantsCache: RestaurantsCache,
        private val domainDbMapper: DomainDbMapper
) : IRestaurantDetailsRepository {

    // todo cached restaurant
    override fun getCachedRestaurant(id: String): Single<RestaurantDetails> {
       /* return Single.just(RestaurantDetails(id, "Kebab",
                50.450532, 30.522990, 4.3f, 17))*/

        return Single.error(Exception("a"))
    }

    override fun getRestaurantDetails(id: String): Single<RestaurantDetails> {
        return shaurmapApi.getRestaurantDetails(id)
    }
}