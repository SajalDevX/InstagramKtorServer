package com.mrsajal.repository.follows

import com.mrsajal.dao.follows.FollowsDao
import com.mrsajal.dao.user.UserDao
import com.mrsajal.dao.user.UserRow
import com.mrsajal.model.FollowAndUnfollowResponse
import com.mrsajal.model.FollowUserData
import com.mrsajal.model.GetFollowsResponse
import com.mrsajal.util.Constants
import com.mrsajal.util.Response
import io.ktor.http.*

class FollowsRepositoryImpl(
    private val userDao: UserDao,
    private val followsDao: FollowsDao
) : FollowsRepository {
    override suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        return if (followsDao.isAlreadyFollowing(follower, following)) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "You are Already following this user"
                )
            )
        } else {
            val success = followsDao.followUser(follower, following)
            if (success) {
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Response.Success(
                    data = FollowAndUnfollowResponse(success = true)
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = FollowAndUnfollowResponse(
                        success = false,
                        message = "Error in  FollowsDaoImpl Else"
                    )
                )
            }
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        val success = followsDao.unfollowUser(follower, following)

        return if (success) {
            userDao.updateFollowsCount(follower, following, isFollowing = false)
            Response.Success(
                data = FollowAndUnfollowResponse(success = true)
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "Oops Something went wrong, Try again later"
                )
            )
        }
    }

    override suspend fun getFollowers(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse> {
        val followersIds = followsDao.getFollowers(userId, pageNumber, pageSize)
        val followersRows = userDao.getUsers(ids = followersIds)
        val followers = followersRows.map { followerRow ->
            val isFollowing = followsDao.isAlreadyFollowing(follower = userId, following = followerRow.id)
            toFollowUserData(userRow = followerRow, isFollowing = isFollowing)
        }
        return Response.Success(
            data = GetFollowsResponse(success = true, follows = followers)
        )
    }

    override suspend fun getFollowing(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse> {
        val followingIds = followsDao.getFollowing(userId, pageNumber, pageSize)
        val followingRows = userDao.getUsers(ids = followingIds)
        val following = followingRows.map { followingRow ->
            toFollowUserData(userRow = followingRow, isFollowing = true)
        }
        return Response.Success(
            data = GetFollowsResponse(success = true, follows = following)
        )
    }

    override suspend fun getFollowingSuggestions(userId: Long): Response<GetFollowsResponse> {
        val hasFollowing = followsDao.getFollowing(userId = userId, pageNumber = 0, pageSize = 1).isNotEmpty()
        return if (hasFollowing) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = GetFollowsResponse(success = false, message = "User has following")
            )
        } else {
            val suggestedFollowingRows = userDao.getPopularUsers(limit = Constants.SUGGESTED_FOLLOWING_LIMIT)
            val suggestedFollowing = suggestedFollowingRows.filterNot {
                it.id == userId
            }.map {
                toFollowUserData(userRow = it, isFollowing = false)
            }
            return Response.Success(
                data = GetFollowsResponse(success = true, follows = suggestedFollowing)
            )
        }
    }

    private fun toFollowUserData(userRow: UserRow, isFollowing: Boolean): FollowUserData {
        return FollowUserData(
            id = userRow.id,
            name = userRow.name,
            bio = userRow.bio,
            imageUrl = userRow.imageUrl,
            isFollowing = isFollowing
        )
    }
}