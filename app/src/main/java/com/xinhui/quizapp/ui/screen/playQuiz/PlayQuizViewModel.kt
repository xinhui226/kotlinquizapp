package com.xinhui.quizapp.ui.screen.playQuiz

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.repo.QuizRepo
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
    private val quizRepo: QuizRepo
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
}