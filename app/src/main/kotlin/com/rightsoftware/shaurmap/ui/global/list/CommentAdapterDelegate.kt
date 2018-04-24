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
import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.utils.extensions.inflate
import com.rightsoftware.shaurmap.utils.extensions.toLocalizedShortDateString
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapterDelegate(
        private val clickListener: (Comment) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Comment
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CommentViewHolder(parent.inflate(R.layout.item_comment), clickListener)
    }

    override fun onBindViewHolder(items: MutableList<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        return (holder as CommentViewHolder).bind(items[position] as Comment)
    }

    private class CommentViewHolder(val view: View, clickListener: (Comment) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var comment: Comment

        init {
            view.setOnClickListener { clickListener.invoke(comment) }
        }

        fun bind(comment: Comment) {
            this.comment = comment
            with(comment) {
                with(view) {
                    GlideApp.with(context)
                            .load(user.thumbnailUrl)
                            .apply(RequestOptions().circleCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.avatar_placeholder_circle)
                            .into(ivCommentatorhumbnail)

                    tvCommentatorName.text = user.displayName
                    tvCommentDate.text = date.toLocalizedShortDateString()
                    tvComment.text = text
                }
            }
        }
    }
}