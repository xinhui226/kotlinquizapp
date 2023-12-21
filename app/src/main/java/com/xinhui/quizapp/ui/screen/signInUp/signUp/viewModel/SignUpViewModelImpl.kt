package com.xinhui.quizapp.ui.screen.signInUp.signUp.viewModel

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(),SignUpViewModel {

    override fun signUp(name: String, email: String, pwd: String, confirmPwd: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = registrationValidate(name,email,pwd,confirmPwd)
            if(error.isEmpty()){
                val user = safeApiCall { authService.signUp(email,pwd) }
                if(user != null){
                    safeApiCall {
                        userRepo.addNewUser(User(name=name, email = email, group = emptyList())).let {
                            _success.emit("Register successfully") }
                    }
                    _isLoading.emit(false)
                }
            }
            else _error.emit(error)
        }
    }

    private fun registrationValidate(name: String,email:String, pwd:String, confirmPwd:String):String {
        return if (name.isEmpty()||
            email.isEmpty()||
            pwd.isEmpty()||
            confirmPwd.isEmpty())
            "Please fill up all the fields"
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            "Invalid email format"
        else if(name.length < 3)
            "Username must be at least 3 characters"
        else if(pwd.length <= 5)
            "Password must be at least 6 characters"
        else if(pwd != confirmPwd)
            "Password and confirm password isn't match"
        else ""
    }
}