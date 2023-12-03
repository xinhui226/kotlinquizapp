package com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.StudentGroupRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModel @Inject constructor(
    private val studentGroupRepo: StudentGroupRepo,
    private val quizRepo: QuizRepo
): BaseAddEditQuizViewModel() {

    init {
        getGroups()
    }

    override fun getGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                studentGroupRepo.getGroups().collect{
                    _groups.emit(it)
                }
            }
        }
    }
    override fun submit(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            if(quiz.groups.isEmpty()) _error.emit("Select at least one student group to assign.")
            else{
                val error = quizValidation(quiz)
                if (error.isNullOrEmpty()) {
                    _isLoading.emit(true)
                    safeApiCall {
                        quizRepo.addNewQuiz(quiz)
                    }.let {
                        _isLoading.emit(false)
                        _success.emit("Quiz has been created.")
                    }
                } else _error.emit(error)
            }
        }
    }
}