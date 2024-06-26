package com.mrsajal.di

import com.mrsajal.dao.follows.FollowsDao
import com.mrsajal.dao.follows.FollowsDaoImpl
import com.mrsajal.dao.post.PostDao
import com.mrsajal.dao.post.PostDaoImpl
import com.mrsajal.dao.post_comment.PostCommentDao
import com.mrsajal.dao.post_comment.PostCommentDaoImpl
import com.mrsajal.dao.post_likes.PostLikesDao
import com.mrsajal.dao.post_likes.PostLikesDaoImpl
import com.mrsajal.dao.user.UserDao
import com.mrsajal.dao.user.UserDaoImpl
import com.mrsajal.repository.auth.AuthRepository
import com.mrsajal.repository.auth.AuthRepositoryImpl
import com.mrsajal.repository.follows.FollowsRepository
import com.mrsajal.repository.follows.FollowsRepositoryImpl
import com.mrsajal.repository.post.PostRepository
import com.mrsajal.repository.post.PostRepositoryImpl
import com.mrsajal.repository.post_comment.PostCommentRepository
import com.mrsajal.repository.post_comment.PostCommentRepositoryImpl
import com.mrsajal.repository.post_likes.PostLikesRepository
import com.mrsajal.repository.post_likes.PostLikesRepositoryImpl
import com.mrsajal.repository.profile.ProfileRepository
import com.mrsajal.repository.profile.ProfileRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao> { FollowsDaoImpl() }
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
    single<PostLikesDao> { PostLikesDaoImpl() }
    single<PostDao> { PostDaoImpl() }
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<PostCommentDao> { PostCommentDaoImpl() }
    single<PostCommentRepository> { PostCommentRepositoryImpl(get(), get()) }
    single<PostLikesDao> { PostLikesDaoImpl() }
    single<PostLikesRepository> { PostLikesRepositoryImpl(get(), get()) }
}