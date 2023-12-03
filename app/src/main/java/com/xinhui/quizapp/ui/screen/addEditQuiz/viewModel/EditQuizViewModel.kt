package com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.StudentGroupRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    private val studentGroupRepo: StudentGroupRepo,
    private val quizRepo: QuizRepo
): BaseAddEditQuizViewModel() {

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

    override fun getGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                studentGroupRepo.getGroups().collect{groups->
                    val quizGrp = mutableListOf<StudentGroup>()
                    groups.map {
                        if (_quiz.value.groups.contains(it.id)) quizGrp.add(it)
                    }
                    _groups.emit(quizGrp)
                }
            }
        }
    }

    fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            safeApiCall{
                quizRepo.getQuiz(id)?.let{
                    _quiz.emit(it)
                    getGroups()
                }
            }
            _isLoading.emit(false)
        }
    }

    override fun submit(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = quizValidation(quiz)
            if (error.isNullOrEmpty()){
                safeApiCall{
                    quizRepo.updateQuiz(_quiz.value.id!!, _quiz.value.copy(
                        name = quiz.name,
                        date = quiz.date,
                        titles = quiz.titles,
                        options = quiz.options,
                        answers = quiz.answers,
                        seconds = quiz.seconds,
                        groups = _quiz.value.groups,
                        isPublished = quiz.isPublished
                    ))
                }?.let{
                    _success.emit("Update successfully.")
                }
            }else _error.emit(error)
        }
    }

}