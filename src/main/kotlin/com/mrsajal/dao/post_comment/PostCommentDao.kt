package com.mrsajal.dao.post_comment


interface PostCommentDao {
    suspend fun addComment(postId: Long, userId: Long, content: String): PostCommentsRow?
    suspend fun findComment(commentId: Long, postId: Long): PostCommentsRow?
    suspend fun deleteComment(commentId: Long, postId: Long): Boolean
    suspend fun getComments(postId: Long, pageSize: Int, pageNumber: Int): List<PostCommentsRow>
}