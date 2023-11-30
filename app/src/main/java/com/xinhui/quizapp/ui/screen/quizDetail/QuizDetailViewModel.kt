package com.xinhui.quizapp.ui.screen.quizDetail

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
) : BaseViewModel() {
    protected val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(
        name = "",
        date= "",
        titles= listOf(),
        options= listOf(),
        answers= listOf(),
        seconds= listOf(),
        groups= listOf(),
        isPublished = true))
    val quiz: StateFlow<Quiz> = _quiz
    protected val _isOwner: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isOwner: SharedFlow<Boolean> = _isOwner

    fun getQuiz(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                quizRepo.getQuiz(id)?.let{
                    _quiz.emit(it)
                    _isOwner.emit(it.createdBy == authService.getUid())
                }
            }
        }
    }
}