package com.xinhui.quizapp.ui.screen.base

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.xinhui.quizapp.ui.screen.mainActivity.MainActivity
import kotlinx.coroutines.launch

abstract class BaseSignInUpFragment<T: ViewBinding>:MostBaseFragment<T>() {

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.success.collect{
                showSnackbar(it)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}