package com.xinhui.quizapp.ui.screen.base

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.xinhui.quizapp.ui.screen.signInUp.SignInUpActivity
import kotlinx.coroutines.launch

abstract class BaseFragment<T: ViewBinding>:MostBaseFragment<T>() {
    protected lateinit var navController: NavController

    override fun setupFunctionInViewCreated() {
        navController = NavHostFragment.findNavController(this)
        super.setupFunctionInViewCreated()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.success.collect{
                showSnackbar(it)
            }
        }
    }
}