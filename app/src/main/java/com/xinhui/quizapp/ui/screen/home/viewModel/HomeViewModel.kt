package com.xinhui.quizapp.ui.screen.home.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.data.repo.QuizRepo
import com.xinhui.quizapp.data.repo.StudentGroupRepo
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
    private val studentGroupRepo: StudentGroupRepo,
): BaseViewModel() {

    protected val _quizzes: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes

    protected val _groups: MutableStateFlow<List<StudentGroup>> = MutableStateFlow(emptyList())
    val groups: StateFlow<List<StudentGroup>> = _groups

    init {
        getQuizzesOfTheDay()
        getGroups()
    }

    private fun getQuizzesOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                userRepo.getUser()?.let { user->
                    if (user.group.isNotEmpty())
                        safeApiCall{
                        quizRepo.getQuizOfTheDay().collect{
                            _quizzes.emit(it) }
                        }
                }
            }
        }
    }

    private fun getGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                studentGroupRepo.getAllGroups().collect{groups ->
                    Log.d("debugging", "getGroups: $groups")
                    val userGrp = mutableListOf<StudentGroup>()
                    groups.map {
                        if (userRepo.getUser()?.group?.contains(it.id) == true) userGrp.add(it)
                    }
                    _groups.emit(userGrp)
                }
            }
        }
    }

}