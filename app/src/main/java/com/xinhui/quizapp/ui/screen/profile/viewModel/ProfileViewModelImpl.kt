package com.xinhui.quizapp.ui.screen.profile.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.core.service.StorageService
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(
    private val userRepo: UserRepo,
    private val authService: AuthService,
    private val storageService: StorageService,
) : BaseViewModel(),ProfileViewModel {

    override fun updateUserPassword(existPassword: String, newPassword:String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (newPassword.length < 6)
                _error.emit("At least 6 characters for a new password")
            else{
                _isLoading.emit(true)
                authService.updatePassword(
                    existPassword, newPassword
                ) { msg, err ->
                    runBlocking {
                        if(err != null) {
                            _error.emit(err)
                            _isLoading.emit(false)
                        } else {
                            _success.emit(msg)
                            _isLoading.emit(false)
                        }
                    }
                }
            }
        }
    }

    override fun updateProfilePic(uri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            authService.getUid().let {
                val name = "$it.jpg"
                storageService.addImage(name,uri).let {
                    _finish.emit(Unit)
                }
            }
            _isLoading.emit(false)
        }
    }

    fun updateUserDetail(user: User, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            if (name.length < 3)
                _error.emit("At least 3 characters for a new username")
            else if(name != user.name){
                userRepo.updateUserDetail(user.copy(name = name))
                _success.emit("Successfully updated username.")
            }
            _isLoading.emit(false)
        }
    }
}