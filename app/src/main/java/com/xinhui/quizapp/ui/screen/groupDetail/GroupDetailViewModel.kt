package com.xinhui.quizapp.ui.screen.groupDetail

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.data.model.User
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
class GroupDetailViewModel @Inject constructor(
    private val studentGroupRepo: StudentGroupRepo,
    private val userRepo: UserRepo,
    private val quizRepo: QuizRepo
) : BaseViewModel() {

    protected val _group: MutableStateFlow<StudentGroup> = MutableStateFlow(
        StudentGroup(name = "", createdBy = ""))
    val group: StateFlow<StudentGroup> = _group
    protected val _students: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val students: StateFlow<List<User>> = _students
    protected val _quizzes: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes
    protected val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous", group = emptyList()))
    val user: StateFlow<User> = _user

    fun getUser(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            safeApiCall { userRepo.getUser()?.let { user ->
                _user.emit(user)
                }
            }.let {
                getGroup(id)
            }
        }
    }

    fun getGroup(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{
                studentGroupRepo.getGroup(id)?.let{
                    _group.emit(it)
                }
            }
            _isLoading.emit(false)
        }
    }

    fun getStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                _group.value.id?.let {id ->
                    userRepo.getStudentsByGroup(id).collect{
                        _students.emit(it)
                    }
                }
            }
        }
    }

//    fun getOwnQuizzes() {
//        viewModelScope.launch(Dispatchers.IO) {
//            safeApiCall {
//                quizRepo.getOwnQuizzes().collect{
//                    it.filter { quiz ->
//                        (quiz.groups.contains(_group.value.id))
//                    }.let {filterQuiz ->
//                    _quizzes.emit(filterQuiz)
//                    }
//                }
//            }
//        }
//    }

    fun getQuizByGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                quizRepo.getQuizByGrp(_group.value.id!!).collect {
                    _quizzes.emit(it)
                }
            }
        }
    }


    fun updateGroupName(name:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _group.value.id?.let {id ->
                safeApiCall{
                    studentGroupRepo.updateStudentGroup(
                        id,
                        _group.value.copy(name = name))
                }
                _finish.emit(Unit)
                _success.emit("Update successful.")
            }
            _isLoading.emit(false)
        }
    }

    fun removeFromGroup(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val newGrp = user.group.toMutableList()
            _group.value.id?.let { newGrp.remove(it) }
            safeApiCall{ user.id?.let {
                userRepo.updateUserGroup( it,
                    User(name = user.name, email = user.email, group = newGrp)) }
            }
        }
    }
}