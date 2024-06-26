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

    override suspend fun removeComment(commentId: Long, postId: Long): Boolean {
        return dbQuery {
            PostCommentsTable.deleteWhere {
                (PostCommentsTable.commentId eq commentId) and (PostCommentsTable.postId eq postId)
            } > 0
        }
    }

    override suspend fun findComment(commentId: Long, postId: Long): PostCommentsRow? {
        return dbQuery{
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

    override suspend fun getComments(postId: Long, pageSize: Int, pageNumber: Int): List<PostCommentsRow> {
        return dbQuery {
            PostCommentsTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentsTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select(where = {PostCommentsTable.postId eq postId})
                .orderBy(column = PostCommentsTable.createdAt, SortOrder.DESC)
                .limit(n = pageSize, offset =((pageNumber - 1) * pageSize).toLong() )
                .map { toPostCommentRow(it) }
        }
    }


    private fun toPostCommentRow(resultRow: ResultRow): PostCommentsRow{
        return PostCommentsRow(
            commentId = resultRow[PostCommentsTable.commentId],
            content = resultRow[PostCommentsTable.content],
            postId = resultRow[PostCommentsTable.postId],
            userId = resultRow[PostCommentsTable.userId],
            userName = resultRow[UserTable.name],
            userImageUrl = resultRow[UserTable.imageUrl],
            createdAt = resultRow[PostCommentsTable.createdAt].toString()
        )
    }
}