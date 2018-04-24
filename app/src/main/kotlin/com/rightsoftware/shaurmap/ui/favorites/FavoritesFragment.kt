package com.rightsoftware.shaurmap.ui.favorites

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.favorites.FavoritesPresenter
import com.rightsoftware.shaurmap.presentation.favorites.FavoritesView
import com.rightsoftware.shaurmap.ui.global.base.BaseFragment
import com.rightsoftware.shaurmap.ui.global.list.FavoriteRestaurantAdapterDelegate
import com.rightsoftware.shaurmap.ui.global.list.ProgressAdapterDelegate
import com.rightsoftware.shaurmap.ui.global.list.ProgressItem
import com.rightsoftware.shaurmap.utils.extensions.animateFadeIn
import com.rightsoftware.shaurmap.utils.extensions.animateFadeOut
import com.rightsoftware.shaurmap.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.design.longSnackbar
import toothpick.Toothpick

class FavoritesFragment : BaseFragment(), FavoritesView {
    override val layoutRes = R.layout.fragment_favorites
    private val adapter = FavoritesAdapter()

    @InjectPresenter lateinit var presenter: FavoritesPresenter

    @ProvidePresenter
    fun providePresenter() : FavoritesPresenter {
        return Toothpick.openScope(DI.BUSINESS_SCOPE)
                .getInstance(FavoritesPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener { presenter.refreshFavoriteRestaurants() }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshFavoriteRestaurants()
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        if(show) {
            pbFullscreen.animateFadeIn(300)
        } else {
            pbFullscreen.animateFadeOut(300)
        }

        swipeToRefresh.visible(!show)
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }

    }

    override fun showEmptyView(show: Boolean) {
        // todo this stuff
    }

    override fun showEmptyError(show: Boolean, message: String?) {

    }

    override fun showMessage(message: String) {
        longSnackbar(view!!, message)
    }

    override fun showFavorites(show: Boolean, favorites: List<FavoriteRestaurant>) {
        recyclerView.post { adapter.setData(favorites) }
    }

    inner class FavoritesAdapter : ListDelegationAdapter<MutableList<Any>>() {
        private val adapterDelegate = FavoriteRestaurantAdapterDelegate({ presenter.onRestaurantClicked(it.restaurant) })

        init {
            items = mutableListOf()
            delegatesManager.addDelegate(adapterDelegate)
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(favoriteRestaurants: List<FavoriteRestaurant>) {
            val progress = isProgress()

            items.clear()
            items.addAll(favoriteRestaurants)
            if (progress) items.add(ProgressItem())

            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?,
                                      position: Int,
                                      payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 2) presenter.loadNextPage()
        }
    }
}