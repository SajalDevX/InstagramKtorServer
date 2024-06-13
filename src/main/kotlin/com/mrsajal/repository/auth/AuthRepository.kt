package com.mrsajal.repository.auth

import com.mrsajal.model.AuthResponse
import com.mrsajal.model.SignInParams
import com.mrsajal.model.SignUpParams
import com.mrsajal.util.Response

interface AuthRepository{
    suspend fun signUp(params:SignUpParams): Response<AuthResponse>
    suspend fun signIn(params:SignInParams):Response<AuthResponse>
}