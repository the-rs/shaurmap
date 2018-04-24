package com.rightsoftware.shaurmap.data.restaurants.cache

import com.rightsoftware.shaurmap.data.Restaurant
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.reactivex.Single
import io.objectbox.rx.RxQuery
import javax.inject.Inject

class RestaurantsCache @Inject constructor(boxStore: BoxStore)  {

    private val restaurantsBox by lazy { boxStore.boxFor<Restaurant>() }

    fun getRestaurants() : Single<List<Restaurant>> {
        return RxQuery.observable(restaurantsBox.query().build())
                .first(ArrayList())
    }

    fun getRestaurant(id: Long) : Single<Restaurant> {
        return Single.just(restaurantsBox.get(id))
    }

    fun updateRestaurants(restaurants: List<Restaurant>) = with(restaurantsBox){
        removeAll()
        put(restaurants)
    }

    fun addRestaurant(restaurant: Restaurant) = restaurantsBox.put(restaurant)
}