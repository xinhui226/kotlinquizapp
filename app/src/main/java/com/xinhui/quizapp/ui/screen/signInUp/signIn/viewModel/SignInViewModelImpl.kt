package com.xinhui.quizapp.ui.screen.signInUp.signIn.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImpl @Inject constructor(
    private val authService: AuthService,
) : BaseViewModel(),SignInViewModel {

    override fun signIn(email: String, pwd: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(email.isEmpty() || pwd.isEmpty())
                _error.emit("Please fill all the field")
            else{
                _isLoading.emit(true)
                val loginUser = safeApiCall { authService.signIn(email,pwd) }
                _isLoading.emit(false)
                if(loginUser!=null) {
                    _success.emit("Login successfully")
                }
            }
        }
    }

}