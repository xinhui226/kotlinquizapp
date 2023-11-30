package com.xinhui.quizapp.data.model

data class Question(
    var titles:String,
    var options: List<String>,
    var answers: String,
    var seconds: Long
)
