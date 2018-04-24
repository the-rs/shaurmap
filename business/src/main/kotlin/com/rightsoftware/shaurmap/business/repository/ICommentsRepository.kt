package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.Comment
import com.rightsoftware.shaurmap.business.SubmitComment
import io.reactivex.Single

interface ICommentsRepository {
    fun getComments(restaurantId: String, page: Int, latestCommentTimestamp: Long?) : Single<List<Comment>>
    fun postComment(comment: SubmitComment) : Single<Comment>
}