package com.rightsoftware.shaurmap.data.comments

import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.business.SubmitComment
import com.rightsoftware.shaurmap.business.repository.ICommentsRepository
import com.rightsoftware.shaurmap.data.ShaurmapApi
import io.reactivex.Single
import javax.inject.Inject

class CommentsRepository @Inject constructor(
        private val shaurmapApi: ShaurmapApi
) : ICommentsRepository {
    override fun getComments(
            restaurantId: String,
            page: Int,
            latestCommentTimestamp: Long?) : Single<List<Comment>> {
        return shaurmapApi.getComments(restaurantId, page, latestCommentTimestamp)
    }

    override fun postComment(comment: SubmitComment) : Single<Comment> {
        return shaurmapApi.postComment(comment)
    }
}