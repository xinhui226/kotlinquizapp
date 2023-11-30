package com.xinhui.quizapp.ui.screen.profile.viewModel

import android.net.Uri

interface ProfileViewModel {
    fun updateProfilePic(uri: Uri)
    fun updateUserPassword(existPassword: String,newPassword:String)
}