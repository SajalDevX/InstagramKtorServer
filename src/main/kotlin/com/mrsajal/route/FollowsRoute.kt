package com.mrsajal.route

import com.mrsajal.model.FollowAndUnfollowResponse
import com.mrsajal.model.FollowsParams
import com.mrsajal.repository.follows.FollowsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.followsRouting() {

    val repository by inject<FollowsRepository>()
    authenticate {
        route("/follows") {
            post {
                val params = call.receiveNullable<FollowsParams>()

                if (params == null) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = FollowAndUnfollowResponse(
                            success = false,
                            message = "Error in Route Package "
                        )
                    )
                    return@post
                }
                val result = if (params.isFollowing) {
                    repository.followUser(params.follower, params.following)
                } else {
                    repository.unfollowUser(params.follower, params.following)
                }
                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
    }
}