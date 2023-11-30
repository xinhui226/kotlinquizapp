package com.xinhui.quizapp.ui.screen.quizDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentQuizDetailBinding
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizDetailFragment : BaseFragment<FragmentQuizDetailBinding>() {

    override val viewModel: QuizDetailViewModel by viewModels()
    private val args: QuizDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        viewModel.getQuiz(args.quizId)
        setupAdapter()
        binding.run {
            // TODO: if quiz taken and not the day yet, disabled btnTakeQuiz
            btnTakeQuiz.setOnClickListener {
                val action = QuizDetailFragmentDirections.actionQuizDetailToPlayQuiz(args.quizId)
                navController.navigate(action)
            }
            ivEdit.setOnClickListener {
                val action = QuizDetailFragmentDirections.actionQuizDetailToEditQuiz(args.quizId)
                navController.navigate(action)
            }
            // TODO: rank if empty show no data
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quiz.collect{
                binding.run {
                    tvQuizName.text = it.name
                    tvQuizDate.text = it.date
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isOwner.collect{
                binding.run {
                    if (it) ivEdit.visibility = View.VISIBLE
                    else btnTakeQuiz.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAdapter() {
        // TODO: setup student score adapter
//        val adapter = StudentScoreAdapter(emptyList())
//        binding.rvRank.adapter = adapter
//        binding.rvRank.layoutManager =
//            LinearLayoutManager(requireContext())
    }

}