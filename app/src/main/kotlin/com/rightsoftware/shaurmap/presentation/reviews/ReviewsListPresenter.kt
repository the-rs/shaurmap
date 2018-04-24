package com.rightsoftware.shaurmap.presentation.reviews

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.Review
import com.rightsoftware.shaurmap.business.interactor.reviews.ReviewsInteractor
import com.rightsoftware.shaurmap.presentation.global.Paginator
import javax.inject.Inject
import com.rightsoftware.shaurmap.di.qualifiers.Restaurant as RestaurantQualifier


@InjectViewState
class ReviewsListPresenter @Inject constructor(
        private val interactor: ReviewsInteractor,
        @RestaurantQualifier private val restaurant: Restaurant
) : MvpPresenter<ReviewsListView>() {
    private val paginator = Paginator(
            { interactor.getReviews(restaurant.id, it) },
            object : Paginator.ViewController<Review> {
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

                override fun showData(show: Boolean, data: List<Review>) {
                    viewState.showReviews(show, data)
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showPageProgress(show)
                }
            }
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshReviews()
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }

    fun refreshReviews() = paginator.refresh()

    fun loadNextPage() = paginator.loadNewPage()

    fun onReviewClicked(reviewerId: String){
        // todo open profile page
    }
}