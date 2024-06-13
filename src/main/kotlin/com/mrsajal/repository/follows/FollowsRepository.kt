package com.mrsajal.repository.follows

import com.mrsajal.model.FollowAndUnfollowResponse
import com.mrsajal.util.Response

interface FollowsRepository {
    suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>

    suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>
}