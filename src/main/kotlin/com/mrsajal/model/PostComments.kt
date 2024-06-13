package com.mrsajal.model

import kotlinx.serialization.Serializable

@Serializable
data class NewCommentParams(
    val content: String,
    val postId: Long,
    val userId: Long
)

@Serializable
data class RemoveCommentParams(
    val postId: Long,
    val commentId: Long,
    val userId: Long
)

@Serializable
data class PostComment(
    val content: String,
    val postId: Long,
    val userId: Long,
    val commentId: Long,
    val userName: String,
    val createdAt: String,
    val userImageUrl: String?=null
)

@Serializable
data class CommentResponse(
    val success: Boolean,
    val comment: PostComment? = null,
    val message: String? = null
)
@Serializable
data class GetCommentResponse(
    val success: Boolean,
    val comments: List<PostComment> = listOf(),
    val message: String? = null
)


