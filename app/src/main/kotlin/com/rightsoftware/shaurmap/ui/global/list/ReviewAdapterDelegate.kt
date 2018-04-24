package com.rightsoftware.shaurmap.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.rightsoftware.shaurmap.GlideApp
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.utils.extensions.inflate
import com.rightsoftware.shaurmap.utils.extensions.toLocalizedMediumDateString
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapterDelegate(
        private val clickListener: (Review) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Review
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ReviewViewHolder(parent.inflate(R.layout.item_review), clickListener)
    }

    override fun onBindViewHolder(items: MutableList<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        return (holder as ReviewViewHolder).bind(items[position] as Review)
    }

    private class ReviewViewHolder(val view: View, clickListener: (Review) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var review: Review

        init {
            view.setOnClickListener { clickListener.invoke(review) }
        }

        fun bind(review: Review) {
            this.review = review
            with(review) {
                with(view) {
                    GlideApp.with(context)
                            .load(user.thumbnailUrl)
                            .apply(RequestOptions().circleCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.avatar_placeholder_circle)
                            .into(ivCreatorThumbnail)

                    tvCreatorName.text = user.displayName
                    tvCreatedDate.text = resources.getString(
                            R.string.published_format, date.toLocalizedMediumDateString())

                    rbRating.rating = rating.toFloat()

                    when(taste){
                        0 -> {
                            ivTasteRating.setImageResource(R.drawable.ic_thumb_down_red)
                        }
                        1 -> {
                            ivTasteRating.setImageResource(R.drawable.ic_thumb_up_green)
                        }
                        null -> {
                            ivTasteRating.setImageResource(R.drawable.ic_thumbs_up_down)
                        }
                    }

                    when(service){
                        0 -> {
                            ivServiceRating.setImageResource(R.drawable.ic_thumb_down_red)
                        }
                        1 -> {
                            ivServiceRating.setImageResource(R.drawable.ic_thumb_up_green)
                        }
                        null -> {
                            ivServiceRating.setImageResource(R.drawable.ic_thumbs_up_down)
                        }
                    }
                }
            }
        }
    }
}