package com.mrsajal.repository.post_likes

import com.mrsajal.dao.post.PostDao
import com.mrsajal.dao.post_likes.PostLikesDao
import com.mrsajal.model.LikeParams
import com.mrsajal.model.LikeResponse
import com.mrsajal.util.Response
import io.ktor.http.*

class PostLikesRepositoryImpl(
    private val likesDao: PostLikesDao,
    private val postDao: PostDao
) : PostLikesRepository {
    override suspend fun addLike(params: LikeParams): Response<LikeResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = LikeResponse(
                    success = false,
                    message = "Post already liked by user"
                )
            )
        } else {
            val postLiked = likesDao.addLike(postId = params.postId, userId = params.userId)
            if (postLiked) {
                postDao.updateLikesCount(params.postId)
                Response.Success(
                    data = LikeResponse(
                        success = true,
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikeResponse(
                        success = false,
                        message = "Error adding like, try again"
                    )
                )
            }
        }
    }

    override suspend fun removeLike(params: LikeParams): Response<LikeResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists) {
            val likeRemoved = likesDao.removeLike(postId = params.postId, userId = params.userId)
            if (likeRemoved) {
                postDao.updateLikesCount(params.postId,decrement = true)
                Response.Success(
                    data = LikeResponse(
                        success = true,
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikeResponse(
                        success = false,
                        message = "Unexpected error occurred"
                    )
                )
            }
        } else {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = LikeResponse(
                    success = false,
                    message = "Like does not exist, may be removed already"
                )
            )
        }
    }
}