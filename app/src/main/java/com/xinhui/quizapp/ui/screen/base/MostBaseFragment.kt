package com.xinhui.quizapp.ui.screen.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.xinhui.quizapp.R
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import com.xinhui.quizapp.ui.screen.loadingAnim.LoadingDialogFragment
import com.xinhui.quizapp.ui.screen.signInUp.SignInUpActivity
import kotlinx.coroutines.launch

abstract class MostBaseFragment<T: ViewBinding>:Fragment() {
    protected lateinit var binding: T
    protected abstract val viewModel: BaseViewModel
    private val loadingDialogFragment by lazy { LoadingDialogFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFunctionInViewCreated()
    }

    protected open fun setupFunctionInViewCreated(){
        setupUIComponents()
        setupViewModelObserver()
    }

    protected open fun setupViewModelObserver() {
        lifecycleScope.launch {
            viewModel.error.collect{
                showSnackbar(it,true)
            }
        }
        lifecycleScope.launch{
            viewModel.isLoading.collect{
                if(it && !loadingDialogFragment.isAdded)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, "loader")
                else if(!it && loadingDialogFragment.isAdded)
                    loadingDialogFragment.dismissAllowingStateLoss()
            }
        }
    }

    protected open fun setupUIComponents(){}

    fun showSnackbar(msg: String,isError:Boolean = false) {
        val sb = Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_LONG
        )
        if(isError)
            sb.setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.error)
            )
        sb.show()
    }
}