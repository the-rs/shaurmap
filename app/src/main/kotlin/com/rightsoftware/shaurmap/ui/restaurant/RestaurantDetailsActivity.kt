package com.rightsoftware.shaurmap.ui.restaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.MarkerOptions
import com.androidmapsextensions.OnMapReadyCallback
import com.androidmapsextensions.SupportMapFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.db.chart.animation.Animation
import com.db.chart.model.Bar
import com.db.chart.model.BarSet
import com.db.chart.renderer.AxisRenderer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.like.LikeButton
import com.like.OnLikeListener
import com.r0adkll.slidr.model.SlidrPosition
import com.rightsoftware.shaurmap.GlideApp
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.business.RestaurantDetails
import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.business.extensions.progressFrom
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.restaurant.RestaurantDetailsPresenter
import com.rightsoftware.shaurmap.presentation.restaurant.RestaurantView
import com.rightsoftware.shaurmap.ui.global.SlidableActivity
import com.rightsoftware.shaurmap.ui.global.custom.MyReviewView
import com.rightsoftware.shaurmap.ui.global.list.CommentAdapterDelegate
import com.rightsoftware.shaurmap.ui.global.list.ProgressAdapterDelegate
import com.rightsoftware.shaurmap.ui.global.list.ProgressItem
import com.rightsoftware.shaurmap.ui.reviews.ReviewsListActivity
import com.rightsoftware.shaurmap.utils.extensions.*
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import kotlinx.android.synthetic.main.activity_restaurant.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import toothpick.Toothpick
import javax.inject.Inject
import com.rightsoftware.shaurmap.di.qualifiers.Restaurant as RestaurantQualifier


class RestaurantDetailsActivity : SlidableActivity(), OnMapReadyCallback, RestaurantView {
    override val layoutRes = R.layout.activity_restaurant
    override val sliderPosition = SlidrPosition.TOP

    private lateinit var map: GoogleMap

    private val snackProgressBarManager by lazy {
        SnackProgressBarManager(contentView!!).setProgressBarColor(R.color.colorPrimary)
    }

    private val commentsAdapter = CommentsAdapter()

    @InjectPresenter
    lateinit var presenter: RestaurantDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): RestaurantDetailsPresenter {
        return Toothpick.openScopes(DI.RESTAURANT_DETAILS_SCOPE)
                .getInstance(RestaurantDetailsPresenter::class.java)
    }

    @Inject
    lateinit var navigationHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, Toothpick.openScope(DI.BUSINESS_SCOPE))

        presenter.getOwnThumbnail()
        presenter.refreshComments()

        (supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment)
                .getExtendedMapAsync(this)

        // todo unlock slider while dragging if possible
        appBarLayout.addOnOffsetChangedListener { _, offset ->
            if (offset == 0) slider.unlock()
            else slider.lock()
        }

        btnAllReviews.setOnClickListener {
            presenter.showAllReviews()
        }

        viewMyReview.run {
            setOnSubmitClickListener {
                presenter.submitReview(it.rating, it.taste, it.service)
                it.setDisplayMode(MyReviewView.DisplayMode.PREVIEW, animate = true)
            }
            setOnEditClickListener {
                it.setDisplayMode(MyReviewView.DisplayMode.SUBMIT, animate = true)
            }
            setOnDeleteClickListener {
                // todo delete
                it.resetValues()
                it.setDisplayMode(MyReviewView.DisplayMode.SUBMIT, animate = true)
            }
        }


        rvComments.run {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = commentsAdapter
        }

        ivBtnPostComment.setOnClickListener {
            presenter.postComment(etComment.text.toString())
        }

        btnToggleFavorite.setOnLikeListener(object: OnLikeListener{
            override fun liked(p0: LikeButton?) { presenter.addRestaurantToFavorites() }
            override fun unLiked(p0: LikeButton?) { presenter.removeRestaurantFromFavorites() }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFinishing) Toothpick.closeScope(DI.RESTAURANT_DETAILS_SCOPE)
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.setAllGesturesEnabled(false)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        presenter.onMapLoaded()
    }

    override fun setRestaurantPosition(position: LatLng) {
        map.run {
            addMarker(MarkerOptions().position(position))
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
        }
    }

    override fun setRestaurantDetails(restaurantDetails: RestaurantDetails) {
        llContent.animateFadeIn(500)

        restaurantDetails.run {
            GlideApp.with(this@RestaurantDetailsActivity)
                    .load(creator.thumbnailUrl)
                    .apply(RequestOptions().circleCrop())
                    .transition(withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.avatar_placeholder_circle)
                    .into(ivCreatorThumbnail)

            tvCreatorName.text = creator.displayName
            tvCreatedDate.text = getString(R.string.published_format, createdDate.toLocalizedMediumDateString())
            tvAvgRating.text = avgRating.toString()
            rbAvgRating.rating = avgRating.toFloat()
            tvReviewsCount.text = totalReviewsCount.toString()
            tvCommentsCount.text = commentsCount.toString()

            viewMyReview.run {
                myReview.let {
                    if (it == null) {
                        setDisplayMode(MyReviewView.DisplayMode.SUBMIT)
                    } else {
                        setDisplayMode(MyReviewView.DisplayMode.PREVIEW)
                        setReview(it)
                    }
                }
            }


            tvTasteLikes.text = tasteLikes.toString()
            tvTasteDislikes.text = tasteDislikes.toString()
            tvServiceLikes.text = serviceLikes.toString()
            tvServiceDislikes.text = serviceDislikes.toString()

            val barSet = BarSet().apply {
                addBar(Bar("1", oneStarReviews.toFloat()).apply { color = getResColor(R.color.lightRed) })
                addBar(Bar("2", twoStarsReviews.toFloat()).apply { color = getResColor(R.color.orange) })
                addBar(Bar("3", threeStarsReviews.toFloat()).apply { color = getResColor(R.color.yellow) })
                addBar(Bar("4", fourStarsReviews.toFloat()).apply { color = getResColor(R.color.bogGreen) })
                addBar(Bar("5", fiveStarsReviews.toFloat()).apply { color = getResColor(R.color.green) })
            }

            chartRating.run {
                addData(barSet)
                setXLabels(AxisRenderer.LabelPosition.NONE)
                setRoundCorners(5f)
                if (totalReviewsCount > 0) show(Animation(600))
            }

            val tasteOpinionsCount = tasteLikes + tasteDislikes
            val serviceOpinionsCount = serviceLikes + serviceDislikes

            if (tasteOpinionsCount > 0) {
                pbTasteLikes.progress = (tasteLikes / tasteOpinionsCount) * 100
                pbTasteLikes.progress = tasteLikes.progressFrom(tasteOpinionsCount)
            }

            if (serviceOpinionsCount > 0) {
                pbServiceLikes.progress = (serviceLikes / serviceOpinionsCount) * 100
                pbServiceLikes.progress = serviceLikes.progressFrom(serviceOpinionsCount)
            }

            btnToggleFavorite.isLiked = isInFavorites
        }
    }

    override fun showReviewPublicationProgress(show: Boolean) {
        if (show) {
            snackProgressBarManager.show(
                    SnackProgressBar(SnackProgressBar.TYPE_INDETERMINATE,
                            getString(R.string.posting))
                            .setAllowUserInput(true), SnackProgressBarManager.LENGTH_INDEFINITE)
        } else {
            snackProgressBarManager.dismiss()
        }
    }

    override fun onReviewPublished(review: Review) {
        viewMyReview.setReview(review)
        longSnackbar(contentView!!, R.string.review_has_been_published, R.string.show_all) {
            presenter.showAllReviews()
        }
    }

    override fun onFavoriteStateChanged(addedToFavorites: Boolean) {
        snackbar(contentView!!,
                if(addedToFavorites) R.string.added_to_favorites
                else R.string.removed_from_favorites)
    }

    override fun showError(message: String) {
        showSnackMessage(message)
    }

    override fun showLoading(loading: Boolean) {
        if (loading) pbLoading.visibility = View.VISIBLE
        else pbLoading.visibility = View.GONE
    }

    override fun setOwnThumbnail(thumbnailUrl: String) {
        viewMyReview.setThumbnailUrl(thumbnailUrl)

        GlideApp.with(this)
                .load(thumbnailUrl)
                .apply(RequestOptions().circleCrop())
                .transition(withCrossFade())
                .placeholder(R.drawable.avatar_placeholder_circle)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivOwnCommentThumbnail)
    }

    override fun showComments(comments: List<Comment>) {
        rvComments.post { commentsAdapter.setData(comments) }
    }

    override fun showCommentsRefreshProgress(show: Boolean) {
        if (show) {
            pbComments.animateFadeIn(300)
            rvComments.animateFadeOut(300)
        } else {
            pbComments.animateFadeOut(300)
            rvComments.animateFadeIn(300)
        }
    }

    override fun showEmptyProgress(show: Boolean) {
        if (show) {
            pbComments.animateFadeIn(300)
        } else {
            pbComments.animateFadeOut(300)
        }
    }

    override fun showCommentsPageProgess(show: Boolean) {
        rvComments.post { commentsAdapter.showProgress(show) }
    }

    override fun showCommentsEmptyView(show: Boolean) {
    }

    override fun showCommentsEmptyError(show: Boolean, message: String?) {
    }

    override fun showMessage(message: String) {
        longSnackbar(contentView!!, message)
    }

    override fun showComments(show: Boolean, comments: List<Comment>) {
        rvComments.post { commentsAdapter.setData(comments) }
    }

    override fun onCommentPublished(comment: Comment) {
        hideKeyboard()
        etComment.run {
            text.clear()
            removeFocus()
        }
        presenter.refreshComments()
    }

    override fun showCommentPublicationProgress(show: Boolean) {
        if (show) {
            ivBtnPostComment.visible(false)
            pbPostComment.visible(true)
        } else {
            ivBtnPostComment.visible(true)
            pbPostComment.visible(false)
        }
    }

    inner class CommentsAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(CommentAdapterDelegate({ }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(comments: List<Comment>) {
            val progress = isProgress()

            items.clear()
            items.addAll(comments)
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

            if (position == items.size - 2) presenter.loadNextCommentsPage()
        }
    }

    private val navigator = object : SupportAppNavigator(this, R.id.fullScreenContainer) {
        override fun createActivityIntent(context: Context, screenKey: String?, data: Any?) = when (screenKey) {
            Screens.REVIEWS_SCREEN ->
                Intent(this@RestaurantDetailsActivity, ReviewsListActivity::class.java)

            else -> null
        }

        override fun createFragment(screenKey: String?, data: Any?) = null
    }
}
