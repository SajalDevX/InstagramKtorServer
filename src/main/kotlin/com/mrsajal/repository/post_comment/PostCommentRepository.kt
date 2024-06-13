package com.mrsajal.repository.post_comment

import com.mrsajal.model.*
import com.mrsajal.util.Response

interface PostCommentRepository {
    suspend fun addComment(params: NewCommentParams):Response<CommentResponse>
    suspend fun getPostComments(postId: Long,pageNumber: Int,pageSize:Int):Response<GetCommentResponse>
    suspend fun removeComment(params: RemoveCommentParams):Response<CommentResponse>
}