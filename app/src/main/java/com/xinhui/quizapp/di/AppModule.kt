package com.xinhui.quizapp.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quiz.core.service.AuthServiceImpl
import com.xinhui.quizapp.core.service.StorageService
import com.xinhui.quizapp.core.service.StorageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideStorageService(): StorageService = StorageServiceImpl()

    @Provides
    @Singleton
    fun provideUserRef():CollectionReference {
        return FirebaseFirestore.getInstance().collection("users")
    }
}