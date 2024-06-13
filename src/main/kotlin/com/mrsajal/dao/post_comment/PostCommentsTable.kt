package com.mrsajal.dao.post_comment

import com.mrsajal.dao.post.PostTable
import com.mrsajal.dao.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PostCommentsTable : Table(name = "post_comments"){
    val commentId = long(name = "comment_id").uniqueIndex()
    val postId = long(name = "post_id").references(ref = PostTable.postId, onDelete = ReferenceOption.CASCADE)
    val userId = long(name = "user_id").references(ref = UserTable.id, onDelete = ReferenceOption.CASCADE)
    val content = varchar(name = "content", length = 300)
    val createdAt = datetime(name = "created_at").defaultExpression(defaultValue = CurrentDateTime)
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