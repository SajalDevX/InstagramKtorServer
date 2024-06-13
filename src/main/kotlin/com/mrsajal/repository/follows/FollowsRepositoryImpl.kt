package com.mrsajal.repository.follows

import com.mrsajal.dao.follows.FollowsDao
import com.mrsajal.dao.user.UserDao
import com.mrsajal.model.FollowAndUnfollowResponse
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
                    message = "Error in  FollowsDaoImpl"
//                    "You are Already following this user"
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
//                        "Oops Something went wrong, Try again later"
                    )
                )
            }
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        val success = followsDao.unfollowUser(follower, following)

        return if (success) {
            userDao.updateFollowsCount(follower,following,isFollowing = false)
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
}