package com.mrsajal.dao.post_comment

import com.mrsajal.dao.DatabaseFactory.dbQuery
import com.mrsajal.dao.user.UserTable
import com.mrsajal.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostCommentDaoImpl : PostCommentDao {
    override suspend fun addComment(postId: Long, userId: Long, content: String): PostCommentsRow? {
        return dbQuery {
            val commentId = IdGenerator.generateId()
            PostCommentsTable.insert {
                it[PostCommentsTable.commentId] = commentId
                it[PostCommentsTable.postId] = postId
                it[PostCommentsTable.userId] = userId
                it[PostCommentsTable.content] = content
            }
            findComment(commentId, postId)
        }
    }

    override suspend fun findComment(commentId: Long, postId: Long): PostCommentsRow? {
        return dbQuery {
            PostCommentsTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentsTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { (PostCommentsTable.postId eq postId) and (PostCommentsTable.commentId eq commentId) }
                .singleOrNull()
                ?.let { toPostCommentRow(it) }
        }
    }

    private fun toPostCommentRow(row: ResultRow): PostCommentsRow {
        return PostCommentsRow(
            commentId = row[PostCommentsTable.commentId],
            postId = row[PostCommentsTable.postId],
            userId = row[PostCommentsTable.userId],
            content = row[PostCommentsTable.content],
            userName = row[UserTable.name],
            userImageUrl = row[UserTable.imageUrl],
            createdAt = row[PostCommentsTable.createdAt].toString(),
        )
    }

    override suspend fun deleteComment(commentId: Long, postId: Long): Boolean {
        return dbQuery {
            PostCommentsTable.deleteWhere {
                (PostCommentsTable.commentId eq commentId) and (PostCommentsTable.postId eq postId)
            } > 0
        }
    }

    override suspend fun getComments(postId: Long, pageSize: Int, pageNumber: Int): List<PostCommentsRow> {

        return dbQuery {
            PostCommentsTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentsTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { PostCommentsTable.postId eq postId }
                .orderBy(PostCommentsTable.createdAt, SortOrder.DESC)
                .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                .map { toPostCommentRow(it) }
        }
    }
}