package com.xinhui.quizapp.data.repo

import com.xinhui.quizapp.data.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepo {
    suspend fun getGrpUsersScoreByQuiz(isOwner: Boolean,quizId: String): Flow<List<Score>>
    suspend fun addScore(score: Score)
    suspend fun getUserHistoryQuiz(): Flow<List<String>>
}