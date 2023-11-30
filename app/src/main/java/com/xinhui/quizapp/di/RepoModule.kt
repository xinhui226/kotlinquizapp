package com.xinhui.quizapp.di

import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.QuizRepoImpl
import com.xinhui.quizapp.data.repo.StudentGroupRepo
import com.xinhui.quizapp.data.repo.StudentGroupRepoImpl
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule{

    @Provides
    @Singleton
    fun provideUserRepo(authService: AuthService):UserRepo{
        return UserRepoImpl(authService)
    }

    @Provides
    @Singleton
    fun provideStudentGroupRepo(authService: AuthService): StudentGroupRepo {
        return StudentGroupRepoImpl(authService)
    }

    @Provides
    @Singleton
    fun provideQuizRepo(authService: AuthService,userRepo: UserRepo): QuizRepo {
        return QuizRepoImpl(authService,userRepo)
    }
}