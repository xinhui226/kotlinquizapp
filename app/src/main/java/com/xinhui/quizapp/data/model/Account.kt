package com.xinhui.quizapp.data.model

import android.net.Uri

data class Account(
    val info: User,
    val notValidToAdd: Boolean,
    val profile: Uri?
)
