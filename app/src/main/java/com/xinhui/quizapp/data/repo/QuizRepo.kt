package com.xinhui.quizapp.data.repo

import com.xinhui.quizapp.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {
    suspend fun getOwnQuizzes(): Flow<List<Quiz>>
    suspend fun getQuizOfTheDay(): Flow<List<Quiz>>
    suspend fun getQuiz(id: String): Quiz?
    suspend fun addNewQuiz(quiz: Quiz)
    suspend fun updateQuiz(id: String,quiz: Quiz)
    suspend fun deleteQuiz(id:String)
    suspend fun getQuizByGrp(group: String): Flow<MutableList<Quiz>>
}