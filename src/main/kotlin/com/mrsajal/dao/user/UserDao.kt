package com.mrsajal.dao.user

import com.mrsajal.model.SignUpParams

interface UserDao {
    suspend fun getPopularUsers(limit: Int): List<UserRow>
     suspend fun getUsers(ids: List<Long>): List<UserRow>
    suspend fun insert(params: SignUpParams): UserRow?
    suspend fun findByEmail(email: String): UserRow?
    suspend fun updateFollowsCount(follower: Long, following: Long, isFollowing: Boolean): Boolean
    suspend fun findById(userId: Long): UserRow?
    suspend fun updateUser(userId: Long, name: String, bio: String, imageUrl: String?): Boolean
}