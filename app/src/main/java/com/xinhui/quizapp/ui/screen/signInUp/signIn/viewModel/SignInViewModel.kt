package com.xinhui.quizapp.ui.screen.signInUp.signIn.viewModel

import android.net.Uri
import com.xinhui.quizapp.data.model.User

interface SignInViewModel {
    fun signIn(email:String,pwd:String)
    fun addUser(user: User, profileUri: Uri?)
}