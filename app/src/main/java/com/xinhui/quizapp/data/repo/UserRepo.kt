package com.xinhui.quizapp.data.repo

import com.xinhui.quizapp.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun getUser():User?
    suspend fun addNewUser(user: User)
    suspend fun updateUserDetail(user: User)
    suspend fun deleteUser(id:String)
    suspend fun getStudentsByGroup(group:String):Flow<List<User>>
    suspend fun searchAcc(keyword:String):Flow<List<User>>
    suspend fun updateUserGroup(id: String, user: User)
}