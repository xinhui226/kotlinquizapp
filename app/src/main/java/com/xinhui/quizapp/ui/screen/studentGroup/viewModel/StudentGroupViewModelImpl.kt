package com.xinhui.quizapp.ui.screen.studentGroup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.data.repo.StudentGroupRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentGroupViewModelImpl @Inject constructor(
    private val studentGroupRepo: StudentGroupRepo
): BaseViewModel(),StudentGroupViewModel {

    protected val _groups: MutableStateFlow<List<StudentGroup>> = MutableStateFlow(emptyList())
    val groups: StateFlow<List<StudentGroup>> = _groups

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

    override fun addNewGroup(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.length < 3) _error.emit("At least 3 characters for a new group name")
            else safeApiCall { studentGroupRepo.addNewGroup(name) }
        }
    }
}