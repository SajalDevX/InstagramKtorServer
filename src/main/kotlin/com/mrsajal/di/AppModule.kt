package com.mrsajal.di

import com.mrsajal.dao.user.UserDao
import com.mrsajal.dao.user.UserDaoImpl
import com.mrsajal.repository.UserRepository
import com.mrsajal.repository.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}