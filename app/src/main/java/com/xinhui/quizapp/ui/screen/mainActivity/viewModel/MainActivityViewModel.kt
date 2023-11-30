package com.xinhui.quizapp.ui.screen.mainActivity.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.core.service.StorageService
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainActivityViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val storageService: StorageService,
    private val authService: AuthService
): BaseViewModel(){

    protected val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous", group = emptyList()))
    val user: StateFlow<User> = _user
    protected val _profilePic = MutableStateFlow<Uri?>(null)
    val profilePic: StateFlow<Uri?> = _profilePic

    init {
        getCurrUser()
        getProfileUri()
    }

    fun getCurrUser(){
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall { userRepo.getUser() }?.let { user ->
                _user.emit(user)
            }
        }
    }

    fun getProfileUri(){
        viewModelScope.launch(Dispatchers.IO) {
            authService.getCurrUser()?.uid.let {id ->
                _profilePic.value = storageService.getImage("$id.jpg")
            }
        }
    }

    fun logout() {
        authService.logout()
    }

}