package com.xinhui.quizapp.core.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.xinhui.quizapp.R
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.Score
import com.xinhui.quizapp.databinding.FragmentPlayQuizBinding
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class PlayQuiz(
    private val context: Context,
    private var binding: FragmentPlayQuizBinding,
    private val quiz: Quiz,
    private val finishQuiz:(Score)->Unit,
    private val back:()->Unit,
) {
        private var countDownTimer: CountDownTimer? = null
        private var defaultColor: ColorStateList? = null
        var score = 0
        var correct = 0
        var wrong = 0
        var skip = 0
        private var qIndex = 0
        private var updateQueNo = 1
        private var questions = quiz.titles
        private var answer = quiz.answers
        private var options = quiz.options
        private var seconds = quiz.seconds
        private var stop = false
        private var finished = false

        private fun finishQuiz(){
            finished = true
            binding.nextQuestionBtn.text = "Finish"
            Log.d("debugging", "finishQuiz: $correct, ${questions.size}")
            score = ((correct.toDouble()/questions.size.toDouble()) * 100.0).roundToInt()
            countDownTimer?.cancel()
            finishQuiz(
                Score(quizScore = score, correct, wrong, skip, "","",quiz.id!!, emptyList()))
            binding.nextQuestionBtn.setOnClickListener { back() }
        }

        private fun showNextQuestion() {
            if (stop) checkAnswer()
            else {
                startCountDown(seconds[qIndex])
                binding.apply {
                    if (updateQueNo < questions.size) {
                        tvNoOfQues.text = "${updateQueNo + 1}/${questions.size}"
                        updateQueNo++
                    }
                    if (qIndex <= questions.size - 1) {
                        tvQuestion.text = questions[qIndex]
                        radioButton1.text = options[qIndex * 4] // 2*4=8
                        radioButton2.text = options[qIndex * 4 + 1] //  2*4+1=9
                        radioButton3.text = options[qIndex * 4 + 2] //  2*4+2=10
                        radioButton4.text = options[qIndex * 4 + 3] //  2*4+3=11
                    }
                    radiogrp.clearCheck()
                    radioButton1.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
                    radioButton2.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
                    radioButton3.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
                    radioButton4.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
                }
            }
        }
        private fun checkAnswer() {
            countDownTimer?.cancel()
            binding.apply {
                val correctRB = when(answer[qIndex]) {
                    radioButton1.text.toString() -> radioButton1
                    radioButton2.text.toString() -> radioButton2
                    radioButton3.text.toString() -> radioButton3
                    else -> radioButton4
                }
                if (radiogrp.checkedRadioButtonId == -1) {
                    skip += 1
                    correctRB.setBackgroundColor(ContextCompat.getColor(context, R.color.error))
                }
                else {
                    correctRB.setBackgroundColor(ContextCompat.getColor(context, R.color.success))
                    val checkRadioButton =
                        (binding.root).findViewById<RadioButton>(radiogrp.checkedRadioButtonId)
                    val checkAnswer = checkRadioButton.text.toString()
                    if (checkAnswer == answer[qIndex]) correct += 1
                    else {
                        wrong += 1
                        checkRadioButton.setBackgroundColor(ContextCompat.getColor(context, R.color.error))
                    }
                }
            }
            qIndex++
            if(qIndex == questions.size) finishQuiz()
        }

        fun initViews() {
            binding.apply {
                tvQuestion.text = questions[qIndex]
                radioButton1.text = options[0]
                radioButton2.text = options[1]
                radioButton3.text = options[2]
                radioButton4.text = options[3]
                nextQuestionBtn.setOnClickListener {
                    if (!finished){
                        if(!stop && radiogrp.checkedRadioButtonId == -1)
                            Snackbar
                                .make(
                                    binding.root,
                                    "Please select one answer",
                                    Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.RED)
                                .show()
                        else {
                            stop = !stop
                            nextQuestionBtn.text = if (stop) "Next" else "Submit"
                            showNextQuestion()
                        }
                    }
                }
                tvNoOfQues.text = "$updateQueNo/${questions.size}"
                tvQuestion.text = questions[qIndex]
                defaultColor = quizTimer.textColors
                startCountDown(quiz.seconds[qIndex])
            }
        }

        private fun startCountDown(secondPerQuestion:Long) {
            countDownTimer = object : CountDownTimer((secondPerQuestion+1)*1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.apply {
                        val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toInt()
                        quizTimer.text = String.format(Locale.getDefault(), "Time: %02d", second)
                        if (millisUntilFinished < 5000) quizTimer.setTextColor(Color.RED)
                        else quizTimer.setTextColor(defaultColor)
                    }
                }
                override fun onFinish() {
                    stop = !stop
                    binding.nextQuestionBtn.text = if(stop) "Next" else "Submit"
                    showNextQuestion()
                }
            }.start()
        }
}
