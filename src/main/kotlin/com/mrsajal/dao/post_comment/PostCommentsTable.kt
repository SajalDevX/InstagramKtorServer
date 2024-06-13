package com.mrsajal.dao.post_comment

import com.mrsajal.dao.post.PostTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PostCommentsTable : Table(name = "post_comments") {
    val commentId = long("comment_id").uniqueIndex()
    val postId = long("post_id").references(ref = PostTable.postId, onDelete = ReferenceOption.CASCADE)
    val userId = long("user_id").references(ref = PostTable.postId, onDelete = ReferenceOption.CASCADE)
    val content = varchar("content", length = 300)
    val createdAt = datetime("created_at").defaultExpression(defaultValue = CurrentDateTime)
}

data class PostCommentsRow(
    val commentId:Long,
    val postId:Long,
    val userId:Long,
    val content:String,
    val createdAt:String,
    val userImageUrl:String?,
    val userName:String,
)