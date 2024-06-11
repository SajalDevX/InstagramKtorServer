package com.mrsajal.repository

import com.mrsajal.dao.user.UserDao
import com.mrsajal.model.AuthResponse
import com.mrsajal.model.AuthResponseData
import com.mrsajal.model.SignInParams
import com.mrsajal.model.SignUpParams
import com.mrsajal.plugins.generateToken
import com.mrsajal.security.hashPassword
import com.mrsajal.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExists(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict, data = AuthResponse(
                    errorMessage = "A user with this email already exists"
                )
            )
        } else {
            val insertedUser = userDao.insert(params)
            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError, data = AuthResponse(
                        errorMessage = "Oops we could not register the user, Try later!"
                    )
                )
            } else {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            bio = insertedUser.bio,
                            token = generateToken(params.email)
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findByEmail(params.email)
        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid Credential, no user with this email"
                )
            )
        } else {
            val hashedPassword = hashPassword(password = params.password)
            if (user.password == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = user.id,
                            name = user.name,
                            bio = user.bio,
                            token = generateToken(params.email)
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid Credentials, wrong password"
                    )
                )
            }
        }
    }

    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}