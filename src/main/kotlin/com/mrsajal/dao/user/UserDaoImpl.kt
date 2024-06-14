package com.mrsajal.dao.user

import com.mrsajal.dao.DatabaseFactory.dbQuery
import com.mrsajal.model.SignUpParams
import com.mrsajal.dao.user.UserTable.id
import com.mrsajal.security.hashPassword
import com.mrsajal.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): UserRow? {
        return dbQuery {
            val insertStatement = UserTable.insert {
                it[id] = IdGenerator.generateId()
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }
            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }
    }

    override suspend fun findByEmail(email: String): UserRow? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun updateFollowsCount(follower: Long, following: Long, isFollowing: Boolean): Boolean {
        return dbQuery {
            val count = if (isFollowing) +1 else -1
            val sucess1 = UserTable.update({ UserTable.id eq follower }) {
                it.update(column = followingCount, value = followingCount.plus(count))
            } > 0
            val sucess2 = UserTable.update({ UserTable.id eq following }) {
                it.update(column = followerCount, value = followerCount.plus(count))
            } > 0
            sucess1 && sucess2
        }
    }

    override suspend fun findById(userId: Long): UserRow? {
        return dbQuery {
            UserTable.select { UserTable.id eq userId }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }
    override suspend fun getPopularUsers(limit: Int): List<UserRow> {
        return dbQuery {
            UserTable.selectAll()
                .orderBy(column = UserTable.followerCount, order = SortOrder.DESC)
                .limit(n = limit)
                .map { rowToUser(it) }
        }
    }
    override suspend fun getUsers(ids: List<Long>): List<UserRow> {
        return dbQuery {
            UserTable.select(where = { UserTable.id inList ids})
                .map { rowToUser(it) }
        }
    }


    override suspend fun updateUser(userId: Long, name: String, bio: String, imageUrl: String?): Boolean {
        return dbQuery {
            UserTable.update(where = {UserTable.id eq userId}) {
                it[UserTable.name] = name
                it[UserTable.bio] = bio
                it[UserTable.imageUrl] = imageUrl
            } > 0
        }
    }

    private fun rowToUser(row: ResultRow): UserRow {
        return UserRow(
            id = row[id],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            imageUrl = row[UserTable.imageUrl],
            password = row[UserTable.password],
            followingCount = row[UserTable.followingCount],
            followerCount = row[UserTable.followerCount],
        )
    }
}