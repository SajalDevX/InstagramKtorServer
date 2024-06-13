package com.mrsajal.repository.post_comment

import com.mrsajal.dao.post.PostDao
import com.mrsajal.dao.post_comment.PostCommentDao
import com.mrsajal.dao.post_comment.PostCommentsRow
import com.mrsajal.model.*
import com.mrsajal.util.Response
import io.ktor.http.*

class PostCommentRepositoryImpl(
    private val commentDao: PostCommentDao,
    private val postDao: PostDao
) : PostCommentRepository {
    override suspend fun addComment(params: NewCommentParams): Response<CommentResponse> {
        val postCommentRow = commentDao.addComment(
            postId = params.postId,
            userId = params.userId,
            content = params.content
        )

        return if (postCommentRow == null){
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = CommentResponse(success = false, message = "Could not insert comment into the db")
            )
        }else{
            postDao.updateCommentCount(postId = params.postId)
            Response.Success(
                data = CommentResponse(success = true, comment = toPostComment(postCommentRow))
            )
        }
    }

    private fun toPostComment(commentRow: PostCommentsRow): PostComment{
        return PostComment(
            commentId = commentRow.commentId,
            postId = commentRow.postId,
            content = commentRow.content,
            userId = commentRow.userId,
            createdAt = commentRow.createdAt,
            userName = commentRow.userName,
            userImageUrl = commentRow.userImageUrl

        )
    }

    override suspend fun getPostComments(postId: Long, pageNumber: Int, pageSize: Int): Response<GetCommentResponse> {
        val commentRows = commentDao.getComments(postId = postId, pageNumber = pageNumber, pageSize = pageSize)
        val comments = commentRows.map {
            toPostComment(it)
        }

        return Response.Success(
            data = GetCommentResponse(success = true, comments = comments)
        )
    }


    override suspend fun removeComment(params: RemoveCommentParams): Response<CommentResponse> {
        val commentRow = commentDao.findComment(commentId = params.commentId, postId = params.postId)

        return if (commentRow == null){
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = CommentResponse(success = false, message = "Comment ${params.commentId} not found")
            )
        }else{
            val postOwnerId = postDao.getPost(postId = params.postId)!!.userId

            if (params.userId != commentRow.userId && params.userId != postOwnerId){
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = CommentResponse(
                        success = false,
                        message = "User ${params.userId} cannot delete comment ${params.commentId}"
                    )
                )
            }else{
                val commentWasRemoved = commentDao.removeComment(commentId = params.commentId, postId = params.postId)

                if (commentWasRemoved){
                    postDao.updateCommentCount(postId = params.postId, decrement = true)
                    Response.Success(
                        data = CommentResponse(success = true)
                    )
                }else{
                    Response.Error(
                        code = HttpStatusCode.Conflict,
                        data = CommentResponse(
                            success = false,
                            message = "Comment ${params.commentId} could not be removed"
                        )
                    )
                }
            }
        }
    }
}