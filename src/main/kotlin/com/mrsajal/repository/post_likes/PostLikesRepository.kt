package com.mrsajal.repository.post_likes

import com.mrsajal.model.LikeParams
import com.mrsajal.model.LikeResponse
import com.mrsajal.util.Response

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Response<LikeResponse>
    suspend fun removeLike(params:LikeParams): Response<LikeResponse>
}