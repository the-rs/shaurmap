package com.rightsoftware.shaurmap.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.rightsoftware.shaurmap.GlideApp
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.FavoriteRestaurant
import com.rightsoftware.shaurmap.utils.extensions.inflate
import com.rightsoftware.shaurmap.utils.extensions.toLocalizedMediumDateString
import kotlinx.android.synthetic.main.item_favorite_restaurant.view.*

class FavoriteRestaurantAdapterDelegate(
        private val clickListener: (FavoriteRestaurant) -> Unit
) : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is FavoriteRestaurant
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return FavoriteRestaurantViewHolder(
                parent.inflate(R.layout.item_favorite_restaurant), clickListener)
    }

    override fun onBindViewHolder(items: MutableList<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        return (holder as FavoriteRestaurantViewHolder).bind(items[position] as FavoriteRestaurant)
    }

    private class FavoriteRestaurantViewHolder(
            val view: View, clickListener: (FavoriteRestaurant) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private lateinit var favoriteRestaurant: FavoriteRestaurant

        init {
            view.setOnClickListener { clickListener.invoke(favoriteRestaurant) }
        }

        fun bind(favoriteRestaurant: FavoriteRestaurant) {
            this.favoriteRestaurant = favoriteRestaurant
            with(favoriteRestaurant) {
                with(view) {
                    tvDate.text = resources.getString(
                            R.string.added_format, addedToFavoritesDate.toLocalizedMediumDateString())

                    GlideApp.with(context)
                            .load(mapPictureUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(ivMap)
                }
            }
        }
    }
}