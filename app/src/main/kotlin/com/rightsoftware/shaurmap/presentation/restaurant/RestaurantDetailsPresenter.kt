package com.rightsoftware.shaurmap.presentation.restaurant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.business.SubmitComment
import com.rightsoftware.shaurmap.business.SubmitReview
import com.rightsoftware.shaurmap.business.interactor.restaurant.RestaurantDetailsInteractor
import com.rightsoftware.shaurmap.presentation.global.Paginator
import com.rightsoftware.shaurmap.utils.extensions.position
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import com.rightsoftware.shaurmap.di.qualifiers.Restaurant as RestaurantQualifier

@InjectViewState
class RestaurantDetailsPresenter @Inject constructor(
        private val interactor: RestaurantDetailsInteractor,
        private val router: Router,
        @RestaurantQualifier private val restaurant: Restaurant
): MvpPresenter<RestaurantView>() {
    fun onMapLoaded() {
        viewState.setRestaurantPosition(restaurant.position)

        interactor.getRestaurantDetails(restaurant.id)
                .doOnSubscribe{ viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe({
                    viewState.setRestaurantDetails(it)
                },{
                    it.printStackTrace()
                })
    }

    fun showAllReviews(){
        router.navigateTo(Screens.REVIEWS_SCREEN)
    }

    fun submitReview(rating: Int, taste: Int?, service: Int?){
        interactor.submitReview(SubmitReview(restaurant.id, rating, taste, service))
                .doOnSubscribe{ viewState.showReviewPublicationProgress(true) }
                .doAfterTerminate { viewState.showReviewPublicationProgress(false) }
                .subscribe({
                    viewState.onReviewPublished(it)
                },{
                    it.printStackTrace()
                })
    }

    fun getOwnThumbnail() {
        interactor.getOwnThumbnailUrl()
                .subscribe({
                    viewState.setOwnThumbnail(it)
                },{
                    it.printStackTrace()
                })
    }

    fun refreshComments() {
        paginator.refresh()
    }

    fun postComment(commentText: String){
        interactor.postComment(SubmitComment(restaurant.id, commentText))
                .doOnSubscribe{ viewState.showCommentPublicationProgress(true) }
                .doAfterTerminate { viewState.showCommentPublicationProgress(false) }
                .subscribe({
                    viewState.onCommentPublished(it)
                },{
                    it.printStackTrace()
                })
    }

    fun addRestaurantToFavorites() {
        interactor.addRestaurantToFavorites(restaurant.id)
                .subscribe({
                    viewState.onFavoriteStateChanged(addedToFavorites = true)
                },{
                    it.printStackTrace()
                    // todo message
                })
    }

    fun removeRestaurantFromFavorites() {
        interactor.removeRestaurantFromFavorites(restaurant.id)
                .subscribe({
                    viewState.onFavoriteStateChanged(addedToFavorites = false)
                },{
                    it.printStackTrace()
                    // todo message
                })
    }

    fun loadNextCommentsPage() = paginator.loadNewPage()

    private val paginator = Paginator(
            { interactor.getComments(restaurant.id, it) },
            object : Paginator.ViewController<Comment> {
                override fun showEmptyProgress(show: Boolean) {
                    viewState.showEmptyProgress(show)
                }

                override fun showEmptyError(show: Boolean, error: Throwable?) {
                    if (error != null) {
                        viewState.showCommentsEmptyError(show, error.message)
                    } else {
                        viewState.showCommentsEmptyError(show, null)
                    }
                }

                override fun showErrorMessage(error: Throwable) {
                    viewState.showMessage(error.message!!)
                }

                override fun showEmptyView(show: Boolean) {
                    viewState.showCommentsEmptyView(show)
                }

                override fun showData(show: Boolean, data: List<Comment>) {
                    viewState.showComments(show, data)
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showCommentsRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showCommentsPageProgess(show)
                }
            }
    )
}