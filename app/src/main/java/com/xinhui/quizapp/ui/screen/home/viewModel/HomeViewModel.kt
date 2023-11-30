package com.xinhui.quizapp.ui.screen.home.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
): BaseViewModel() {

    protected val _quizzes: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes

    init {
        getQuizzesOfTheDay()
    }

    private fun getQuizzesOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                userRepo.getUser()?.let { user->
                    if (user.group.isNotEmpty())
                        safeApiCall{
                        quizRepo.getGrpQuiz().collect{
                            Log.d("debugging", "getQuizzes: $it")
                            _quizzes.emit(it) }
                        }
                }
            }
        }
    }
}