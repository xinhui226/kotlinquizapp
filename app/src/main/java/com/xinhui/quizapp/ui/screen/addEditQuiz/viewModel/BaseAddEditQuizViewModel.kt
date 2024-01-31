package com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseAddEditQuizViewModel: BaseViewModel(){

    protected val _groups: MutableStateFlow<List<StudentGroup>> = MutableStateFlow(emptyList())
    val groups: StateFlow<List<StudentGroup>> = _groups
    protected val _questions: MutableStateFlow<List<Question>> = MutableStateFlow(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    abstract fun getGroups()

    fun quizValidation(quiz: Quiz):String? {
        return if(quiz.name.isEmpty()) "Quiz name can't be empty."
        else if(quiz.date.isEmpty()) "Select a date for quiz."
        else if (quiz.titles.isEmpty() &&
            quiz.options.isEmpty() &&
            quiz.answers.isEmpty() &&
            quiz.seconds.isEmpty())
            "CSV file is not yet uploaded"
        else if (quiz.titles.isEmpty() ||
            quiz.options.isEmpty() ||
            quiz.answers.isEmpty() ||
            quiz.seconds.isEmpty() ||
            quiz.titles.contains("") ||
            quiz.options.contains("") ||
            quiz.answers.contains(""))
            "Please make sure all fields are filled."
        else if (quiz.seconds.contains(0))
            "Invalid answer question seconds."
        else null
    }

    fun readCSV(lines:List<String>) {
        viewModelScope.launch {
            lines.map { line ->
                val question = line.split(",")
                if(question.size < 7) {
                    _error.emit("Invalid format of CSV file. Please refer to the example that provided.")
                    null
                }
                else if(question[6].toLongOrNull() == null) {
                    _error.emit("Invalid type for column seconds")
                    null
                }
                else{
                    Question(
                        titles = question[0],
                        options = listOf(question[1],question[2],question[3],question[4]),
                        answers = question[5],
                        seconds = question[6].toLong()
                    )
                }
            }.toList().let {
                if (!it.contains(null)){
                    _questions.emit(emptyList())
                    _questions.emit(
                        it.map {question ->
                            question!! }.toList())
                }
            }
        }
    }

    abstract fun submit(quiz: Quiz)
}