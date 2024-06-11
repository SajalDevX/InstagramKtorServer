package com.mrsajal.route

import com.mrsajal.model.AuthResponse
import com.mrsajal.model.SignInParams
import com.mrsajal.model.SignUpParams
import com.mrsajal.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authRouting() {
    val repository by inject<UserRepository>()

    route(path = "/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials!"
                    )
                )
                return@post
            }
            val result = repository.signUp(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
    route(path = "/login") {
        post {
            val params = call.receiveNullable<SignInParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials!"
                    )
                )
                return@post
            }
            val result = repository.signIn(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}