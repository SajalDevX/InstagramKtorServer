package com.mrsajal.dao.post

interface PostDao {
    suspend fun createPost(caption: String, imageUrl: String, userId: Long): Boolean
    suspend fun getFeedPost(userId: Long, follows: List<Long>, pageNumber: Int, pageSize: Int): List<PostRow>
    suspend fun getPostByUser(userId: Long, pageNumber: Int, pageSize: Int): List<PostRow>
    suspend fun deletePost(postId: Long): Boolean
    suspend fun getPost(postId: Long): PostRow?
    suspend fun updateLikesCount(postId: Long, decrement: Boolean = false): Boolean
//    suspend fun updateCaption(postId: Long, caption: String): Boolean
    suspend fun updateCommentCount(postId: Long, decrement: Boolean = false): Boolean
}