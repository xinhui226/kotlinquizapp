package com.xinhui.quizapp.ui.screen.signInUp.signIn.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.core.service.StorageService
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val storageService: StorageService,
    private val userRepo: UserRepo
) : BaseViewModel(),SignInViewModel {

    fun getAuth(): FirebaseAuth {
        return authService.returnAuth()
    }

    override fun signIn(email: String, pwd: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(email.isEmpty() || pwd.isEmpty())
                _error.emit("Please fill all the field")
            else{
                _isLoading.emit(true)
                safeApiCall { authService.signIn(email,pwd) }
                _isLoading.emit(false)
                _success.emit("")
            }
        }
    }

    override fun addUser(user: User, profileUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            Log.d("debugging", "addUser: $user, $profileUri")
            val existUserData = safeApiCall { userRepo.getUser() }
            Log.d("debugging", "exist user: $existUserData")
            if (existUserData == null){
                safeApiCall { userRepo.addNewUser(user) }
                profileUri?.let{ uri ->
                    authService.getUid().let {
                        val name = "$it.jpg"
                        storageService.addImage(name, uri)
                    }
                }
            }
            _isLoading.emit(false)
            _success.emit("")
        }
    }

}