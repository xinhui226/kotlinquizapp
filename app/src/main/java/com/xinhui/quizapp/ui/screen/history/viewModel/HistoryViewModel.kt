package com.xinhui.quizapp.ui.screen.history.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.ScoreRepo
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val scoreRepo: ScoreRepo,
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {

    protected val _studentHistoryQuizId: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    protected val _quizzes: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes

    init {
        getUserHistoryQuiz()
    }

    private fun getUserHistoryQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                scoreRepo.getUserHistoryQuiz().collect{
                    _studentHistoryQuizId.emit(it)
                    getQuizzes()
                }
            }
        }
    }

    private fun getQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                if (userRepo.getUser()?.group?.isNotEmpty() == true){
                    quizRepo.getQuizzes().collect{quizzes ->
                        val listOfQuiz = mutableListOf<Quiz>()
                        quizzes.map {
                            if (_studentHistoryQuizId.value.contains(it.id)) listOfQuiz.add(it)
                        }
                        _quizzes.emit(listOfQuiz)
                    }
                }
            }
        }
    }
}