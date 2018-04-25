package com.rightsoftware.shaurmap.ui.reviews

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.reviews.ReviewsListPresenter
import com.rightsoftware.shaurmap.presentation.reviews.ReviewsListView
import com.rightsoftware.shaurmap.ui.global.base.BaseActivity
import com.rightsoftware.shaurmap.ui.global.list.EmptyListViewHolder
import com.rightsoftware.shaurmap.ui.global.list.ProgressAdapterDelegate
import com.rightsoftware.shaurmap.ui.global.list.ProgressItem
import com.rightsoftware.shaurmap.ui.global.list.ReviewAdapterDelegate
import com.rightsoftware.shaurmap.utils.extensions.animateFadeIn
import com.rightsoftware.shaurmap.utils.extensions.animateFadeOut
import com.rightsoftware.shaurmap.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_reviews_list.*
import kotlinx.android.synthetic.main.layout_empty_list.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.longSnackbar
import toothpick.Toothpick

class ReviewsListActivity : BaseActivity(), ReviewsListView {
    override val layoutRes = R.layout.activity_reviews_list

    private val adapter = ReviewsAdapter()
    private lateinit var emptyListViewHolder: EmptyListViewHolder

    @InjectPresenter lateinit var presenter: ReviewsListPresenter

    @ProvidePresenter
    fun providePresenter() : ReviewsListPresenter {
        return Toothpick.openScope(DI.RESTAURANT_DETAILS_SCOPE)
                .getInstance(ReviewsListPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener { presenter.refreshReviews() }
        emptyListViewHolder = EmptyListViewHolder(layoutEmptyList, { presenter.refreshReviews() })
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
        if (show) emptyListViewHolder.showEmptyData(getString(R.string.no_reviews_yet))
        else emptyListViewHolder.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) emptyListViewHolder.showEmptyError(message)
        else emptyListViewHolder.hide()
    }

    override fun showMessage(message: String) {
        longSnackbar(contentView!!, message)
    }

    override fun showReviews(show: Boolean, reviews: List<Review>) {
        recyclerView.post { adapter.setData(reviews) }
    }

    inner class ReviewsAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(ReviewAdapterDelegate({ presenter.onReviewClicked(it.user.uid) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(reviews: List<Review>) {
            val progress = isProgress()

            items.clear()
            items.addAll(reviews)
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