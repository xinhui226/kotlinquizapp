package com.xinhui.quizapp.core.service

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    suspend fun signUp(email:String, password:String):FirebaseUser?
    suspend fun signIn(email:String, password:String):FirebaseUser?
    fun getCurrUser():FirebaseUser?
    fun logout()
    fun getUid():String
    fun updatePassword(
        existPassword: String,
        newPassword:String,
        onFinish: (msg: String, err: String?) -> Unit
    )
}