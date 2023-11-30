package com.xinhui.quizapp.ui.screen.manageQuiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.databinding.FragmentManageQuizBinding
import com.xinhui.quizapp.ui.adapter.QuizAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.manageQuiz.viewModel.ManageQuizViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageQuizFragment : BaseFragment<FragmentManageQuizBinding>() {

    override val viewModel: ManageQuizViewModelImpl by viewModels()
    private lateinit var adapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAdapter()
        binding.run {
            btnAdd.setOnClickListener {
                val action = ManageQuizFragmentDirections.actionManageQuizFragmentToAddQuizFragment()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizzes.collect{
                if(it.isNotEmpty()) binding.tvNoData.visibility = View.GONE
                adapter.setNewQuizzes(it)
            }
        }
    }

    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList()){
            val action = ManageQuizFragmentDirections.actionManageQuizToQuizDetail(it.id.toString())
            navController.navigate(action)
        }
        binding.rvQuizzes.adapter = adapter
        binding.rvQuizzes.layoutManager = LinearLayoutManager(requireContext())
    }
}