package com.xinhui.quizapp.ui.screen.signInUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xinhui.quizapp.R
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.databinding.ActivitySignInUpBinding
import com.xinhui.quizapp.ui.screen.mainActivity.MainActivity
import com.xinhui.quizapp.ui.screen.signInUp.signIn.SignInFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInUpActivity : AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService
    private lateinit var binding: ActivitySignInUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = authService.getCurrUser()
        if(currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, SignInFragment()).commit()
    }

}