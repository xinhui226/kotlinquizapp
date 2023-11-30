package com.xinhui.quizapp.ui.screen.searchUser.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.StorageService
import com.xinhui.quizapp.data.model.Account
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.StudentGroupRepo
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
class SearchUserViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val studentGroupRepo: StudentGroupRepo,
    private val storageService: StorageService,
) : BaseViewModel() {

    protected val _users: MutableStateFlow<List<Account>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<Account>> = _users

    protected val _group: MutableStateFlow<StudentGroup> = MutableStateFlow(
        StudentGroup("","",""))
    val group: StateFlow<StudentGroup> = _group

    protected val _noAccFound: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val noAccFound: SharedFlow<Boolean> = _noAccFound

    fun getGroupInfo(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall{ studentGroupRepo.getGroup(id)?.let {
                _group.emit(it)
            } }
        }
    }

    fun searchAcc(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall {
                userRepo.searchAcc(keyword).let {
                    it.collect{ users ->
                        users.map { user ->
                            Account(info = user,
                                notValidToAdd = user.group.contains(_group.value.id) ||
                                        _group.value.createdBy == user.id,
                                profile = storageService.getImage("${user.id}.jpg"))
                        }.toList().let {acc ->
                            _users.emit(acc)
                            _noAccFound.emit(acc.isEmpty())
                        }
                    }
                }
            }
        }
    }

    fun addToGroup(account: Account){
        viewModelScope.launch(Dispatchers.IO) {
            val newGrp = account.info.group.toMutableList()
            _group.value.id?.let { newGrp.add(it) }
            safeApiCall{ account.info.id?.let {
                    userRepo.updateUserGroup( it,
                        User(name = account.info.name, email = account.info.email, group = newGrp)) }
            }?.let { _success.emit("Added to ${_group.value.name}.") }
        }
    }
}