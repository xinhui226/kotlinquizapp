package com.xinhui.quizapp.ui.screen.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentDashboardBinding
import com.xinhui.quizapp.ui.adapter.TeacherContainerAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.mainActivity.MainActivity
import com.xinhui.quizapp.ui.screen.mainActivity.viewModel.MainActivityViewModel
import com.xinhui.quizapp.ui.screen.signInUp.SignInUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragmentFragment : BaseFragment<FragmentDashboardBinding>() {

    override val viewModel: MainActivityViewModel by viewModels(
        ownerProducer = { requireActivity() as MainActivity }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAdapter()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.user.collect{
                binding.tvName.text = it.name
            }
        }
    }

    private fun setupAdapter(){
        val adapter = TeacherContainerAdapter(
            listOf(
                R.id.studentGroupFragment,
                R.id.manageQuizFragment,
                R.id.itemLogout)
        ){ teacherDashNavigate(it) }
        binding.rvTeacherContainer.adapter = adapter
        binding.rvTeacherContainer.layoutManager = GridLayoutManager(requireContext(),2)
    }

    private fun teacherDashNavigate(fragmentId:Int) {
        when(fragmentId) {
            R.id.studentGroupFragment ->
                findNavController().navigate(
                    DashboardFragmentFragmentDirections.actionDashboardToStudentGroup())
            R.id.manageQuizFragment ->
                findNavController().navigate(
                    DashboardFragmentFragmentDirections.actionDashboardToManageQuiz())
            else -> {
                val intent = Intent(requireActivity(), SignInUpActivity::class.java)
                startActivity(intent)
                viewModel.logout()
                requireActivity().finish()
            }
        }
    }

}