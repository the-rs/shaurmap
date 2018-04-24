package com.rightsoftware.shaurmap.presentation.favorites

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.interactor.favorites.FavoritesInteractor
import com.rightsoftware.shaurmap.presentation.global.Paginator
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class FavoritesPresenter @Inject constructor(
        private val interactor: FavoritesInteractor,
        private val router: Router
) : MvpPresenter<FavoritesView>() {
    private val paginator = Paginator(
            { interactor.getFavoritesRestaurants(it) },
            object : Paginator.ViewController<FavoriteRestaurant> {
                override fun showEmptyProgress(show: Boolean) {
                    viewState.showEmptyProgress(show)
                }

                override fun showEmptyError(show: Boolean, error: Throwable?) {
                    if (error != null) {
                        viewState.showEmptyError(show, error.message)
                    } else {
                        viewState.showEmptyError(show, null)
                    }
                }

                override fun showErrorMessage(error: Throwable) {
                    viewState.showMessage(error.message!!)
                }

                override fun showEmptyView(show: Boolean) {
                    viewState.showEmptyView(show)
                }

                override fun showData(show: Boolean, data: List<FavoriteRestaurant>) {
                    viewState.showFavorites(show, data)
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showPageProgress(show)
                }
            }
    )
    
    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }

    fun refreshFavoriteRestaurants() = paginator.refresh()

    fun loadNextPage() = paginator.loadNewPage()

    fun onRestaurantClicked(restaurant: Restaurant){
        router.navigateTo(Screens.RESTAURANT_DETAILS_SCREEN, restaurant)
    }
}