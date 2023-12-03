package com.xinhui.quizapp.ui.screen.quizDetail

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.Score
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.ScoreRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val scoreRepo: ScoreRepo
) : BaseViewModel() {
    protected val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(
        name = "Quiz name",
        date= "(date)",
        titles= listOf(),
        options= listOf(),
        answers= listOf(),
        seconds= listOf(),
        groups= listOf(),
        isPublished = true))
    val quiz: StateFlow<Quiz> = _quiz
    protected val _scores: MutableStateFlow<List<Score>> = MutableStateFlow(emptyList())
    val scores: StateFlow<List<Score>> = _scores
    val userId: String = authService.getUid()

    fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                quizRepo.getQuiz(id)?.let{
                    _quiz.emit(it)
                    getScore()
                }
            }
        }
    }

    private fun getScore() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall { scoreRepo.getGrpUsersScoreByQuiz(_quiz.value.createdBy==userId,_quiz.value.id!!).collect {
                _scores.emit(it) }
            }
        }
    }
}