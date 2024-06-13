package com.mrsajal.model

import kotlinx.serialization.Serializable

@Serializable
data class PostTextParams(
    val caption: String,
    val userId: Long,
)

@Serializable
data class Post(
    val postId: Long,
    val caption: String,
    val userId: Long,
    val createdAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val imageUrl: String,
    val isLiked: Boolean,
    val isOwnPost: Boolean,
    val userName: String,
    val userImageUrl: String?
)

@Serializable
data class PostResponse(
    val success: Boolean,
    val post: Post?=null,
    val message: String? = null
)

@Serializable
data class PostsResponse(
    val success: Boolean,
    val posts: List<Post> = listOf(),
    val message: String? = null
)

@Serializable
data class PostLikeResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class PostCommentResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class PostDeleteResponse(
    val success: Boolean,
    val message: String? = null
)