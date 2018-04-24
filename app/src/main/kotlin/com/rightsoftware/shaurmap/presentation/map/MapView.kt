package com.rightsoftware.shaurmap.presentation.map

import com.arellomobile.mvp.MvpView
import com.rightsoftware.shaurmap.business.Restaurant


interface MapView : MvpView {
    fun addRestaurantsToMap(restaurants: List<Restaurant>)
    fun enterAddNewRestaurantMode()
    fun exitAddNewRestaurantMode()
    fun showEmailIsNotVerifiedError()
    fun onEmailVerificationSent()
    fun showProgress(show: Boolean)
    fun onNewRestaurantCreated(restaurant: Restaurant)
    fun enableLocation()
    fun showTurnOnLocationConfirmation()
}