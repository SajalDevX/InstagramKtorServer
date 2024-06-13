package com.mrsajal.route

import com.mrsajal.model.CommentResponse
import com.mrsajal.model.NewCommentParams
import com.mrsajal.model.RemoveCommentParams
import com.mrsajal.repository.post_comment.PostCommentRepository
import com.mrsajal.util.Constants
import com.mrsajal.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.postCommentsRouting() {
    val repository by inject<PostCommentRepository>()

    authenticate {
        route("/post/comments") {
            post(path = "/create"){
                try {
                    val params = call.receiveNullable<NewCommentParams>()

                    if (params == null){
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = CommentResponse(
                                success = false,
                                message = "Could not parse comment parameters"
                            )
                        )
                        return@post
                    }

                    val result = repository.addComment(params = params)
                    call.respond(status = result.code, message = result.data)
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "An unexpected error occurred, try again!"
                        )
                    )
                    return@post
                }
            }
            delete(path = "/delete") {
                try {
                    val params = call.receiveNullable<RemoveCommentParams>()
                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = CommentResponse(
                                success = false,
                                message = "Could not parse comment parameters"
                            )
                        )
                        return@delete
                    }
                    val result = repository.removeComment(params)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (anyError: Exception) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "An unexpected error has occurred"
                        )
                    )
                }
            }
            get(path = "/{postId}") {
                try {
                    val postId = call.getLongParameter("postId")
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getPostComments(postId, page, limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (anyError: Exception) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "An unexpected error has occurred"
                        )
                    )
                }
            }
        }
    }
}