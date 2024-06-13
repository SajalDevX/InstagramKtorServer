package com.mrsajal.repository.post

import com.mrsajal.model.PostResponse
import com.mrsajal.model.PostTextParams
import com.mrsajal.model.PostsResponse
import com.mrsajal.util.Response

interface PostRepository {
    suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Response<PostResponse>
    suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse>
    suspend fun deletePost(postId: Long): Response<PostResponse>
    suspend fun getPostByUser(
        postOwnerId:Long,
        currentUserId:Long,
        pageNumber: Int,
        pageSize: Int
    ):Response<PostsResponse>
    suspend fun getPost(postId: Long,currentUserId: Long): Response<PostResponse>
}