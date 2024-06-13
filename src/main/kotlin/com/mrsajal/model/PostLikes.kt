package com.mrsajal.model

import kotlinx.serialization.Serializable
@Serializable
data class LikeParams(
    val userId: Long,
    val postId: Long
)
@Serializable
data class LikeResponse(
    val success: Boolean,
    val message: String? = null
)