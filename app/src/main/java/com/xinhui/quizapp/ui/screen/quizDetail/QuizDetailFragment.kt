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
import com.xinhui.quizapp.ui.adapter.StudentScoreAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class QuizDetailFragment : BaseFragment<FragmentQuizDetailBinding>() {

    override val viewModel: QuizDetailViewModel by viewModels()
    private val args: QuizDetailFragmentArgs by navArgs()
    private lateinit var adapter: StudentScoreAdapter

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
            btnTakeQuiz.setOnClickListener {
                val action = QuizDetailFragmentDirections.actionQuizDetailToPlayQuiz(args.quizId)
                navController.navigate(action)
            }
            ivEdit.setOnClickListener {
                val action = QuizDetailFragmentDirections.actionQuizDetailToEditQuiz(args.quizId)
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        binding.run {
            lifecycleScope.launch {
                viewModel.quiz.collect{
                    tvQuizName.text = it.name
                    tvQuizDate.text = it.date
                    if (it.createdBy != null){
                        ivEdit.visibility = if (it.createdBy==viewModel.userId) View.VISIBLE else View.GONE
                        btnTakeQuiz.visibility =
                            if (viewModel.quiz.value.createdBy != viewModel.userId) View.VISIBLE
                            else View.GONE
                    }
                    val formattedCurrentDate = LocalDate.now()
                        .format(DateTimeFormatter
                            .ofPattern("yyyy-MM-dd"))
                    btnTakeQuiz.isEnabled = it.date == formattedCurrentDate
                }
            }
            lifecycleScope.launch {
                viewModel.scores.collect{
                    adapter.setNewRank(it)
                    tvNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    it.find { score -> score.userId == viewModel.userId }?.let {
                        btnTakeQuiz.isEnabled = false
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = StudentScoreAdapter(emptyList(),viewModel.userId)
        binding.rvRank.adapter = adapter
        binding.rvRank.layoutManager =
            LinearLayoutManager(requireContext())
    }

}