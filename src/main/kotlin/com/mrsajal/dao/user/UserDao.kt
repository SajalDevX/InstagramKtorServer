package com.mrsajal.dao.user

import com.mrsajal.model.SignUpParams

interface UserDao {
    suspend fun insert(params: SignUpParams): UserRow?
    suspend fun findByEmail(email:String): UserRow?
}