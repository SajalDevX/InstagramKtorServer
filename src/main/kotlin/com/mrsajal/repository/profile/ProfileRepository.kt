package com.mrsajal.repository.profile

import com.mrsajal.model.ProfileResponse
import com.mrsajal.model.UpdateUserParams
import com.mrsajal.util.Response


interface ProfileRepository {

    suspend fun getUserById(userId: Long, currentUserId: Long): Response<ProfileResponse>

    suspend fun updateUser(updateUserParams: UpdateUserParams): Response<ProfileResponse>
}