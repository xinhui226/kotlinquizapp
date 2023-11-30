package com.xinhui.quizapp.ui.screen.signInUp.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentSignUpBinding
import com.xinhui.quizapp.ui.screen.base.BaseSignInUpFragment
import com.xinhui.quizapp.ui.screen.signInUp.signIn.SignInFragment
import com.xinhui.quizapp.ui.screen.signInUp.signUp.viewModel.SignUpViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseSignInUpFragment<FragmentSignUpBinding>() {

    override val viewModel: SignUpViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            tvLogin.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flContainer, SignInFragment()).commit()
            }
            btnSubmit.setOnClickListener { viewModel.signUp(
                etName.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etConfirmPassword.text.toString(),
            ) }
        }
    }
}