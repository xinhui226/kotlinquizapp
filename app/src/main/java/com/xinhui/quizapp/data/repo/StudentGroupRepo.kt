package com.xinhui.quizapp.data.repo

import com.xinhui.quizapp.data.model.StudentGroup
import kotlinx.coroutines.flow.Flow


interface StudentGroupRepo {
    suspend fun getGroups(): Flow<List<StudentGroup>>
    suspend fun getGroup(id: String): StudentGroup?
    suspend fun addNewGroup(name: String)
    suspend fun updateStudentGroup(id: String,group: StudentGroup)
    suspend fun deleteGroup(id:String)
}