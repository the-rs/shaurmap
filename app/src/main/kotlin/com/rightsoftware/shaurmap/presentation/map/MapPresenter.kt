package com.rightsoftware.shaurmap.presentation.map

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.model.LatLng
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.exceptions.EmailIsNotVerifiedException
import com.rightsoftware.shaurmap.business.interactor.map.MapInteractor
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class MapPresenter @Inject constructor(
        private val interactor: MapInteractor,
        private val router: Router
) : MvpPresenter<MapView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getRestaurants()
                .subscribe({
                    viewState.addRestaurantsToMap(it)
                },{
                    // todo handle no internet
                })

        viewState.enableLocation()
    }

    fun showRestaurantInfo(restaurant: Restaurant){
        router.navigateTo(Screens.RESTAURANT_DETAILS_SCREEN, restaurant)
    }

    fun onAddCancelClick(isApplyVisible: Boolean){
        if(isApplyVisible) {
            viewState.exitAddNewRestaurantMode()
        }
        else {
            viewState.enterAddNewRestaurantMode()
        }
    }

    fun onAddOperationConfirmed(position: LatLng){
        interactor.createRestaurant(Restaurant("", position.latitude, position.longitude))
                .doOnSubscribe {
                    viewState.exitAddNewRestaurantMode()
                    viewState.showProgress(true)
                }
                .doOnDispose { viewState.showProgress(false) }
                .subscribe({
                    viewState.onNewRestaurantCreated(it)
                },{
                    if(it is EmailIsNotVerifiedException){
                        viewState.showEmailIsNotVerifiedError()
                    }
                    else {
                        it.printStackTrace()
                    }
                })
    }

    fun sendEmailVerification(){
        interactor.sendVerificationEmail()
                .subscribe({
                    viewState.onEmailVerificationSent()
                })
    }

    fun onMyLocationButtonClicked() {
        if(!interactor.isLocationEnabled()) viewState.showTurnOnLocationConfirmation()
    }

    fun turnOnLocation() = router.navigateTo(Screens.LOCATION_SETTINGS_SCREEN)
}