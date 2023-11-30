package com.xinhui.quizapp.ui.screen.manageQuiz.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageQuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo
): BaseViewModel(),ManageQuizViewModel {

    protected val _quizzes: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes

    init {
        getQuizzes()
    }

    override fun getQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                quizRepo.getOwnQuizzes().collect{
                    _quizzes.emit(it)
                }
            }
        }
    }
}