package com.xinhui.quizapp.ui.screen.signInUp.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentSignInBinding
import com.xinhui.quizapp.ui.screen.base.BaseSignInUpFragment
import com.xinhui.quizapp.ui.screen.signInUp.signIn.viewModel.SignInViewModelImpl
import com.xinhui.quizapp.ui.screen.signInUp.signUp.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseSignInUpFragment<FragmentSignInBinding>() {
    override val viewModel: SignInViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            tvRegister.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flContainer, SignUpFragment()).commit()
            }
            btnSubmit.setOnClickListener {
                viewModel.signIn(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
    }
}