package com.xinhui.quizapp.ui.screen.playQuiz

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.Score
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.ScoreRepo
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val scoreRepo: ScoreRepo,
    private val userRepo: UserRepo,
): BaseViewModel() {
    protected val _start: MutableStateFlow<Int> = MutableStateFlow(5)
    val start: StateFlow<Int> = _start
    protected val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(
        name = "",
        date= "",
        titles= listOf(),
        options= listOf(),
        answers= listOf(),
        seconds= listOf(),
        groups= listOf(),
        isPublished = false))
    val quiz: StateFlow<Quiz> = _quiz
    protected val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous", group = emptyList()))
    val user: StateFlow<User> = _user

    init {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall { userRepo.getUser() }?.let { user ->
                _user.emit(user)
            }
        }
    }

    fun startCountdown(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(false)
            while (_start.value >= 0){
                _start.emit(_start.value-1)
                delay(1000)
            }
        }
    }

    fun getQuizQuestion(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            safeApiCall{
                quizRepo.getQuiz(id)?.let{
                    _quiz.emit(it)
                }
            }
        }
    }

    fun addScore(score: Score) {
        Log.d("debugging", "addScore: ${score.copy(userId = _user.value.id!!, userName = _user.value.name, userGroups = _user.value.group)}")
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                scoreRepo.addScore(
                    score.copy(
                        userId = _user.value.id!!,
                        userName = _user.value.name,
                        userGroups = _user.value.group))
            }
        }
    }
}