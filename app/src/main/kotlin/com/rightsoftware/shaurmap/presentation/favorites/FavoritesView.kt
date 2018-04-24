package com.rightsoftware.shaurmap.presentation.favorites

import com.arellomobile.mvp.MvpView
import com.rightsoftware.shaurmap.business.FavoriteRestaurant

interface FavoritesView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showMessage(message: String)

    fun showFavorites(show: Boolean, favorites: List<FavoriteRestaurant>)
}