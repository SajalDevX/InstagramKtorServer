package com.mrsajal.repository.follows

import com.mrsajal.model.FollowAndUnfollowResponse
import com.mrsajal.model.GetFollowsResponse
import com.mrsajal.util.Response

interface FollowsRepository {
    suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>
    suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>
    suspend fun getFollowers(userId:Long,pageNumber:Int,pageSize:Int):Response<GetFollowsResponse>
    suspend fun getFollowing(userId:Long,pageNumber:Int,pageSize:Int):Response<GetFollowsResponse>
    suspend fun getFollowingSuggestions(userId: Long): Response<GetFollowsResponse>
}