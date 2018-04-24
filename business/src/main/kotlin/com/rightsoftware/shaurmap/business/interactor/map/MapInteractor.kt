package com.rightsoftware.shaurmap.business.interactor.map

import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.exceptions.EmailIsNotVerifiedException
import com.rightsoftware.shaurmap.business.repository.IDeviceSettingsRepository
import com.rightsoftware.shaurmap.business.repository.IProfileRepository
import com.rightsoftware.shaurmap.business.repository.IRestaurantsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MapInteractor
@Inject constructor(
        private val restaurantsRepository: IRestaurantsRepository,
        private val profileRepository: IProfileRepository,
        private val deviceSettingsRepository: IDeviceSettingsRepository,
        private val schedulers: SchedulersProvider
) {
    fun pullRestaurants() = restaurantsRepository.pullRestaurants()

    fun getRestaurantsFromCache() = restaurantsRepository.getRestaurantsFromCache()

    /**
     * @return Observable that emits cached restaurants from DB and then unique restaurants from API
     */
    fun getRestaurants() : Observable<List<Restaurant>> {
        return Observable.concatEager(listOf(
                getRestaurantsFromCache().toObservable(),
                pullRestaurants().toObservable()))
                .scan { cached, actual ->
                    actual.filter { r ->
                        !cached.any { it.latitude == r.latitude && it.longitude == r.longitude }
                    }
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun createRestaurant(restaurant: Restaurant) : Single<Restaurant> {
        return profileRepository.isEmailVerified()
                .flatMap { emailVerified ->
                    return@flatMap if(emailVerified) {
                        restaurantsRepository.createRestaurant(restaurant).subscribeOn(schedulers.io())
                    }
                    else {
                        throw EmailIsNotVerifiedException()
                    }
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun sendVerificationEmail() : Completable {
        return profileRepository.sendEmailVerification()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun isLocationEnabled() = deviceSettingsRepository.isLocationEnabled()
}